package com.example.orderservice.exceptions;

public class InvalidDeliveryAddressException extends RuntimeException {
    public InvalidDeliveryAddressException(String message) {
        super(message);
    }
}