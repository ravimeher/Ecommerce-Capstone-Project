package com.example.orderservice.exceptions;

public class InvalidOrderUpdateException extends RuntimeException {
    public InvalidOrderUpdateException(String message) {
        super(message);
    }
}