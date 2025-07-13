package org.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private Long orderId;
    private Long amount;
}
