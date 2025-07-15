package com.example.cartservice.service;

import com.example.cartservice.client.ProductServiceClient;
import com.example.cartservice.dtos.*;
import com.example.cartservice.exception.CartItemNotFoundException;
import com.example.cartservice.exception.InvalidProductIdProvidedException;
import com.example.cartservice.exception.ProductOutOfStockException;
import com.example.cartservice.exception.UserCartNotFoundException;
import com.example.cartservice.model.Cart;
import com.example.cartservice.model.CartItem;
import com.example.cartservice.repo.CartItemRepo;
import com.example.cartservice.repo.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepo cartRepository;
    @Autowired
    private CartItemRepo cartItemRepository;
    @Autowired
    private ProductServiceClient productServiceClient;

    @Override
    @Transactional
    public CartResponse addToCart(Long userId, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setItems(new ArrayList<>());
            newCart.setTotalPrice(0.0);
            return newCart;
        });

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst().orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            //get product from product client
            ProductDto productDto = productServiceClient.getProductById(request.getProductId()).getBody();
            if(productDto == null) {
                throw new InvalidProductIdProvidedException("Provided invalid product id to add in cart");
            }
            newItem.setProductId(productDto.getId());
            newItem.setProductName(productDto.getName());
            newItem.setPricePerUnit(productDto.getPrice());
            newItem.setQuantity(request.getQuantity());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        updateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);

        return toCartResponse(savedCart);
    }

    @Override
    public CartResponse updateItemQuantity(Long userId, UpdateCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new UserCartNotFoundException("Cart not found for user: " + userId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Product not found in cart"));

        item.setQuantity(request.getQuantity());
        updateCartTotal(cart);
        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new UserCartNotFoundException("Cart not found for user: " + userId));

        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        if (!removed) {
            throw new CartItemNotFoundException("Product not found in cart");
        }

        updateCartTotal(cart);
        cartRepository.save(cart);
        return toCartResponse(cart);
    }

    @Override
    public CartResponse getUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new UserCartNotFoundException("Cart not found for user: " + userId));
        return toCartResponse(cart);
    }

    @Override
    public void checkoutCart(Long userId, CheckoutRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new UserCartNotFoundException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        // Here you would forward data to OrderService (via Feign, Kafka, etc.)

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);
    }

    private void updateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPricePerUnit())
                .sum();
        cart.setTotalPrice(total);
    }

    private CartResponse toCartResponse(Cart cart) {
        List<CartItemDto> items = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setProductId(item.getProductId());
            itemDto.setProductName(item.getProductName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPricePerUnit(item.getPricePerUnit());
            itemDto.setTotalPrice(item.getQuantity() * item.getPricePerUnit());
            items.add(itemDto);
        }

        CartResponse response = new CartResponse();
        response.setItems(items);
        response.setTotalPrice(cart.getTotalPrice());
        return response;
    }
}
