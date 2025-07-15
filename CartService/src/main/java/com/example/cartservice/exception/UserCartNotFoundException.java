package com.example.cartservice.exception;

public class UserCartNotFoundException extends RuntimeException {
    public UserCartNotFoundException(String message) {
        super(message);
    }
}
