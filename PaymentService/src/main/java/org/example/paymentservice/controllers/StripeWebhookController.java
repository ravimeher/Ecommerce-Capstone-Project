package org.example.paymentservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stripeWebhook")
public class StripeWebhookController {

    ///https://stackoverflow.com/questions/21197622/how-to-receive-webhook-from-stripe-in-java
    @PostMapping
    public void respondToEvent(@RequestBody String event) {
        System.out.println(event);
    }
}
