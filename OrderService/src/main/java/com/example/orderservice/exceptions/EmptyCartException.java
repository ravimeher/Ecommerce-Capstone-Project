package com.example.orderservice.exceptions;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }
}