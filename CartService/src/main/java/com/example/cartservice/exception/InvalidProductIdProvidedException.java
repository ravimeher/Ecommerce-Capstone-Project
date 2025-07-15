package com.example.cartservice.exception;

public class InvalidProductIdProvidedException extends RuntimeException {
    public InvalidProductIdProvidedException(String s) {
        super(s);
    }
}
