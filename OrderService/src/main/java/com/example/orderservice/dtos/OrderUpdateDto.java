package com.example.orderservice.dtos;

import com.example.orderservice.models.OrderStatus;
import com.example.orderservice.models.PaymentStatus;
import lombok.Data;

@Data
public class OrderUpdateDto {
    Long orderId;
    OrderStatus status;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
