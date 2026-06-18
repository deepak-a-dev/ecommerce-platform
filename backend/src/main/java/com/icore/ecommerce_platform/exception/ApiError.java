package com.icore.ecommerce_platform.exception;

import java.time.LocalDateTime;

/**
 * Standard JSON shape returned to clients when a request fails.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {
}