package com.icore.ecommerce_platform.exception;

/**
 * Thrown when attempting to create a resource that already exists
 * (e.g. registering a username that is already taken).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}