package com.icore.ecommerce_platform.exception;

/**
 * Thrown when a requested resource cannot be found
 * (e.g. a product id that does not exist).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
