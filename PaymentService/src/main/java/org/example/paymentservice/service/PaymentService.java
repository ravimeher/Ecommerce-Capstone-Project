package org.example.paymentservice.service;

import org.example.paymentservice.paymentgateway.PaymentGatewayChooser;
import org.example.paymentservice.paymentgateway.PaymentGatewayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentGatewayChooser paymentGatewayChooser;

    public String getPaymentLink(String name,String orderId,String number,Long amount){
        PaymentGatewayStrategy paymentGatewayStrategy = paymentGatewayChooser.getOptimalPaymentGateway();
        return paymentGatewayStrategy.getPaymentLink(name,orderId,number,amount);
    }
}
