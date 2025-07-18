package com.example.orderservice.exceptions;

public class InvalidCartItemException extends RuntimeException {
    public InvalidCartItemException(String message) {
        super(message);
    }
}