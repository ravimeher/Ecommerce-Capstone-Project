package org.example.productcatalogservice.exceptions;

public class UserNotAuthorised extends RuntimeException {
    public UserNotAuthorised(String s) {
        super(s);
    }
}
