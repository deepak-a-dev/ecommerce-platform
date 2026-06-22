package com.icore.ecommerce_platform.dto;

import java.time.LocalDateTime;

public record OrderSummaryDto(
        int orderId,
        LocalDateTime dateOfOrder,
        double total
) {
}