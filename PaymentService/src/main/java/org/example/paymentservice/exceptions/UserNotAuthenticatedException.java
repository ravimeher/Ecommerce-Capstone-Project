package org.example.paymentservice.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String tokenIsNotValid) {
        super(tokenIsNotValid);
    }
}
