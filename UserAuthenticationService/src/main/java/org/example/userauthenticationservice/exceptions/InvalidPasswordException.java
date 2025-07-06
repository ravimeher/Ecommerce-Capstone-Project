package org.example.userauthenticationservice.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String enterValidPassword) {
        super(enterValidPassword);
    }
}
