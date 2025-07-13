package org.example.productcatalogservice.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String noProductForTheId) {
        super(noProductForTheId);
    }
}
