package org.example.userauthenticationservice.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String s) { super(s);  }
}
