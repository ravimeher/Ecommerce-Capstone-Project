package org.example.paymentservice.paymentgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayChooser {
    @Autowired
    private RazorPayPaymentGateway razorPayPaymentGateway;

    @Autowired
    private StripePaymentGateway stripePaymentGateway;

    public PaymentGatewayStrategy getOptimalPaymentGateway() {
        return razorPayPaymentGateway;
    }
}
