package org.example.userauthenticationservice.exceptions;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String roleDoesNotExist) {
        super(roleDoesNotExist);
    }
}
