package com.icore.ecommerce_platform.exception;

/**
 * Thrown when a request is semantically invalid
 * (e.g. an incorrect/expired OTP or mismatched passwords).
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}