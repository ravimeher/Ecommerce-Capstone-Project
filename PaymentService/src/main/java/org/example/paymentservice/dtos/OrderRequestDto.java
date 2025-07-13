package org.example.paymentservice.dtos;

import lombok.Data;

@Data
public class OrderRequestDto {
    Long orderId;
    String paymentStatus;
}
