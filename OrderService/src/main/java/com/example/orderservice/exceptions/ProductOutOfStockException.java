package com.example.orderservice.exceptions;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(String message) {
        super(message);
    }
}