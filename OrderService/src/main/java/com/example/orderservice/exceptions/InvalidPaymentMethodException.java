package com.example.orderservice.exceptions;

public class InvalidPaymentMethodException extends RuntimeException {
    public InvalidPaymentMethodException(String message) {
        super(message);
    }
}