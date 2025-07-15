package com.example.cartservice.exception;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String missingOrMalformedAuthorizationHeader) {
        super(missingOrMalformedAuthorizationHeader);
    }
}
