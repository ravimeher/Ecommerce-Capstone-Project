package com.example.orderservice.services;

import com.example.orderservice.clients.CartServiceClient;
import com.example.orderservice.clients.PaymentServiceClient;
import com.example.orderservice.clients.ProductServiceClient;
import com.example.orderservice.dtos.*;
import com.example.orderservice.exceptions.*;
import com.example.orderservice.models.*;
import com.example.orderservice.repositories.*;
import com.example.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private PaymentServiceClient paymentServiceClient;
    @Autowired
    private CartServiceClient cartServiceClient;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public OrderResponseDto checkout(CheckoutRequestDto requestDto,String token) {
        if (requestDto.getCartId() == null) {
            throw new EmptyCartException("Cart Id is empty.");
        }

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        ResponseEntity<CartResponse> cartResponse = cartServiceClient.viewCart(token);

        if (!cartResponse.getStatusCode().is2xxSuccessful() || cartResponse.getBody() == null) {
            throw new UserCartNotFoundException("Cart not found for ID: " + requestDto.getCartId());
        }

        CartResponse cartDto = cartResponse.getBody();
        List<CartItemDto> cartItems = cartDto.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new EmptyCartException("Cart is empty. Cannot place order.");
        }


        for (CartItemDto item : cartItems) {
            if (item.getProductId() == null || item.getQuantity() <= 0 || item.getPricePerUnit() <= 0) {
                throw new InvalidCartItemException("Invalid cart item: " + item.getProductId());
            }

            // ✅ Fetch product info from ProductService
            ResponseEntity<ProductDto> response = productServiceClient.getProductById(item.getProductId());
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new InvalidCartItemException("Product not found: " + item.getProductId());
            }

            ProductDto productDto = response.getBody();

            // ✅ Validate available stock
            if (productDto.getQuantityAvailable() == null || productDto.getQuantityAvailable() < item.getQuantity()) {
                throw new ProductOutOfStockException("Product '" + productDto.getName() + "' is out of stock or has insufficient quantity.");
            }

            double itemTotal = item.getPricePerUnit() * item.getQuantity();
            totalAmount += itemTotal;

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPricePerUnit());
            orderItems.add(orderItem);
        }

        // ✅ Create Order
        Order order = new Order();
        order.setUserId(requestDto.getUserId());
        order.setDeliveryAddress(requestDto.getDeliveryAddress());
        order.setPaymentMethod(requestDto.getPaymentMethod());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }
        order.setItems(orderItems);

        // ✅ Save order first
        Order savedOrder = orderRepository.save(order);

        // ✅ Now create and save Payment (INITIATED)
        Payment payment = new Payment();
        payment.setOrder(savedOrder); // reference now is to a persisted entity
        payment.setPaymentMethod(requestDto.getPaymentMethod());
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setAmount(totalAmount);
        payment.setTransactionId("TXN-" + System.currentTimeMillis());

        paymentRepository.save(payment);


        // ✅ Call payment service to get payment link
        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setOrderId(savedOrder.getId());
        paymentRequest.setAmount((long) savedOrder.getTotalAmount()); // cast to Long

        ResponseEntity<PaymentResponseDto> paymentResponse =
                paymentServiceClient.initiatePayment(paymentRequest, token); // token must be passed from controller

        if (!paymentResponse.getStatusCode().is2xxSuccessful() || paymentResponse.getBody() == null) {
            throw new RuntimeException("Failed to generate payment link");
        }

        // ✅ Optionally: Save payment URL in DB if needed
        // savedOrder.getPayment().setPaymentLink(paymentResponse.getBody().getPaymentUrl());
        // paymentRepository.save(savedOrder.getPayment());

        // ✅ Add payment link to response
        OrderResponseDto dto = mapToOrderResponseDto(savedOrder);
        dto.setPaymentUrl(paymentResponse.getBody().getPaymentLink());

        // clear cart for the user


        return dto;

    }

        @Override
    public String getOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        return order.getStatus().name();
    }


    @Override
    public List<OrderResponseDto> getOrderHistory(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderResponseDto> responseList = new ArrayList<>();

        for (Order order : orders) {
            responseList.add(mapToOrderResponseDto(order));
        }

        return responseList;
    }
    @Override
    public void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order ID " + orderId + " not found"));
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("For Order ID " + orderId + "No Payment found"));

        if (paymentStatus == null || (!paymentStatus.equals(PaymentStatus.SUCCESS) && !paymentStatus.equals(PaymentStatus.FAILED))) {
            throw new InvalidOrderUpdateException("Unsupported payment status: " + paymentStatus);
        }
        if(paymentStatus == PaymentStatus.FAILED) {
            order.setStatus(OrderStatus.CANCELLED);
            payment.setStatus(PaymentStatus.FAILED);
        }else{
            order.setStatus(OrderStatus.CONFIRMED);// when payment is done
            payment.setStatus(PaymentStatus.SUCCESS);
        }
        payment.setOrder(order);
        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order ID " + orderId + " not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    private OrderResponseDto mapToOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getId());
        dto.setOrderStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrderItemDto> itemDtos = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setProductId(item.getProductId());
            itemDto.setProductName(item.getProductName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            itemDtos.add(itemDto);
        }

        dto.setItems(itemDtos);
        return dto;
    }
}
