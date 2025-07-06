package org.example.userauthenticationservice.exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String message) {

        super(message);
    }
}
