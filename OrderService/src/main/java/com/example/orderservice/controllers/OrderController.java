package com.example.orderservice.controllers;

import com.example.orderservice.clients.UserServiceClient;
import com.example.orderservice.dtos.*;
import com.example.orderservice.exceptions.*;
import com.example.orderservice.services.OrderService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> checkout(
            @RequestBody CheckoutRequestDto checkoutRequestDto,
            @RequestHeader("Authorization") String token
    ) {
        Long userId = extractAndValidateToken(token);

        if (checkoutRequestDto.getCartId() == null ) {
            throw new EmptyCartException("Cart Id is empty. Cannot place order.");
        }

        if (checkoutRequestDto.getDeliveryAddress() == null || checkoutRequestDto.getDeliveryAddress().isBlank()) {
            throw new InvalidDeliveryAddressException("Delivery address must not be blank.");
        }

        if (checkoutRequestDto.getPaymentMethod() == null || checkoutRequestDto.getPaymentMethod().isBlank()) {
            throw new InvalidPaymentMethodException("Payment method must not be blank.");
        }

        checkoutRequestDto.setUserId(userId);
        OrderResponseDto response = orderService.checkout(checkoutRequestDto,token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponseDto>> getOrderHistory(@RequestHeader("Authorization") String token) {
        Long userId = extractAndValidateToken(token);
        List<OrderResponseDto> history = orderService.getOrderHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> trackOrderStatus(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token
    ) {
        extractAndValidateToken(token);
        String status = orderService.getOrderStatus(orderId);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateOrder(@RequestBody OrderRequestDto dto) {
        try {
            orderService.updatePaymentStatus(dto.getOrderId(), dto.getPaymentStatus());
            return ResponseEntity.ok("Payment and Order updated successfully");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        } catch (InvalidOrderUpdateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid update request");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }
    @PutMapping("/updateOrder")
    public ResponseEntity<String> updateOrderStatus(@RequestBody OrderUpdateDto dto) {
        try {
            orderService.updateOrderStatus(dto.getOrderId(), dto.getStatus());
            return ResponseEntity.ok("Order updated successfully");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        } catch (InvalidOrderUpdateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid update request");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }

    private Long extractAndValidateToken(String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            return userServiceClient.validateToken(token).getId();
        } catch (FeignException e) {
            throw new UserNotAuthenticatedException("Invalid or expired token.");
        }
    }
}
