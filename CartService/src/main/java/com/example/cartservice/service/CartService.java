package com.example.cartservice.service;

import com.example.cartservice.dtos.*;

public interface CartService {

    CartResponse addToCart(Long userId, AddToCartRequest request);

    CartResponse updateItemQuantity(Long userId, UpdateCartRequest request);

    CartResponse removeItemFromCart(Long userId, Long productId);

    CartResponse getUserCart(Long userId);

    void checkoutCart(Long userId, CheckoutRequest request);
}
