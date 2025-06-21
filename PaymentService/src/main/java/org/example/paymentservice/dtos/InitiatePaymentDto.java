package org.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiatePaymentDto {
    private String name;
    private String orderId;
    private String phoneNumber;
    private Long amount;

}
