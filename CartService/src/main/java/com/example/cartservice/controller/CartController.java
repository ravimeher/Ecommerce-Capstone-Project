package com.example.cartservice.controller;

import com.example.cartservice.dtos.*;
import com.example.cartservice.service.CartService;
import com.example.cartservice.client.UserServiceClient;
import com.example.cartservice.exception.UserNotAuthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserServiceClient userServiceClient;

    private UserDto authenticate(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UserNotAuthenticatedException("Missing or malformed Authorization header");
        }
        String rawToken = token.replace("Bearer ", "").trim();
        UserDto user = userServiceClient.validateToken(rawToken);
        if (user == null) {
            throw new UserNotAuthenticatedException("Invalid or expired token");
        }
        return user;
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody AddToCartRequest request,
                                                  @RequestHeader("Authorization") String token) {
        UserDto user = authenticate(token);
        CartResponse response = cartService.addToCart(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateQuantity(@RequestBody UpdateCartRequest request,
                                                       @RequestHeader("Authorization") String token) {
        UserDto user = authenticate(token);
        CartResponse response = cartService.updateItemQuantity(user.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long productId,
                                                   @RequestHeader("Authorization") String token) {
        UserDto user = authenticate(token);
        CartResponse response = cartService.removeItemFromCart(user.getId(), productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId,@RequestHeader("Authorization") String token) {
        UserDto user = authenticate(token);
        String response = cartService.clearCart(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CartResponse> viewCart(@RequestHeader("Authorization") String token) {
        UserDto user = authenticate(token);
        CartResponse response = cartService.getUserCart(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("/checkout")
//    public ResponseEntity<String> checkoutCart(@RequestBody CheckoutRequest request,
//                                               @RequestHeader("Authorization") String token) {
//        UserDto user = authenticate(token);
//        cartService.checkoutCart(user.getId(), request);
//        return new ResponseEntity<>("Order placed successfully", HttpStatus.OK);
//    }
}
