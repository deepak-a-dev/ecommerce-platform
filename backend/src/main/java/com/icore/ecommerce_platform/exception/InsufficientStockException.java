package com.icore.ecommerce_platform.exception;

/**
 * Thrown when an order requests more of a product than is currently in stock.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}