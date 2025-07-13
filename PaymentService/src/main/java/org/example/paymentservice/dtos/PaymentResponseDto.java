package org.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {
    private String paymentLink;
    private Long orderId;
    private Long amount;
    private String currency;
    private String status;
}
