package com.icore.ecommerce_platform.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        int orderId,
        String username,
        LocalDateTime dateOfOrder,
        double total,
        List<OrderItemResponseDto> items
) {
}