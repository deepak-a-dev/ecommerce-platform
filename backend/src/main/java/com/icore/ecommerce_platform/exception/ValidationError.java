package com.icore.ecommerce_platform.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Error response for request-body validation failures, with per-field messages.
 */
public record ValidationError(
        LocalDateTime timestamp,
        int status,
        String error,
        Map<String, String> fieldErrors
) {
}