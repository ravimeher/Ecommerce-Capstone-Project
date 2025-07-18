package com.example.orderservice.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String s) {
        super(s);
    }
}
