package org.example.paymentservice.paymentgateway;

import org.example.paymentservice.dtos.PaymentResponseDto;

public interface PaymentGatewayStrategy {
    PaymentResponseDto getPaymentLink(String name, String emailId, Long orderId, Long amount);
}
