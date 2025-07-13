package org.example.productcatalogservice.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String tokenIsNotValid) {
        super(tokenIsNotValid);
    }
}
