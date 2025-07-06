package org.example.userauthenticationservice.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String tokenExpired) {
        super(tokenExpired);
    }
}
