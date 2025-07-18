package com.example.orderservice.clients;

import com.example.orderservice.dtos.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "paymentService")
public interface PaymentServiceClient {

    @PostMapping("/payment")
    ResponseEntity<PaymentResponseDto> initiatePayment(@RequestBody PaymentRequestDto requestDto,
                                                       @RequestHeader("Authorization") String token);
}
