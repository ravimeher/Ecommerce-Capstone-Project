package org.example.paymentservice.paymentgateway;

public interface PaymentGatewayStrategy {
    String getPaymentLink(String name,String orderId,String number,Long amount);
}
