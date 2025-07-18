package com.example.orderservice.dtos;


import com.example.orderservice.models.PaymentStatus;
import lombok.Data;

@Data
public class OrderRequestDto {
    Long orderId;
    PaymentStatus paymentStatus;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
