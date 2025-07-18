package com.example.orderservice.services;

import com.example.orderservice.dtos.CheckoutRequestDto;
import com.example.orderservice.dtos.OrderResponseDto;
import com.example.orderservice.models.OrderStatus;
import com.example.orderservice.models.PaymentStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDto checkout(CheckoutRequestDto requestDto,String token);
    List<OrderResponseDto> getOrderHistory(Long userId);
    String getOrderStatus(Long orderId);
    void updatePaymentStatus(Long orderId, PaymentStatus paymentStatus);

    void updateOrderStatus(Long orderId, OrderStatus status);
}
