package com.example.orderservice.clients;

import com.example.orderservice.dtos.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cartService")
public interface CartServiceClient {

    @GetMapping("/cart")
    ResponseEntity<CartResponse> viewCart(@RequestHeader("Authorization") String token);
    @DeleteMapping("/remove/{productId}")
    ResponseEntity<CartResponse> removeItem(@PathVariable Long productId,
                                            @RequestHeader("Authorization") String token);

}
