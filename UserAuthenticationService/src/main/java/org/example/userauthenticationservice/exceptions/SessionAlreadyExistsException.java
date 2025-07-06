package org.example.userauthenticationservice.exceptions;

public class SessionAlreadyExistsException extends RuntimeException {
    public SessionAlreadyExistsException(String s) {
        super(s);
    }
}
