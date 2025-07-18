package com.example.orderservice.exceptions;

public class UserCartNotFoundException extends RuntimeException {
    public UserCartNotFoundException(String msg) {
        super(msg);
    }
}
