package com.icore.ecommerce_platform.dto;

public record OrderItemResponseDto(
        String productName,
        int qty,
        double unitPrice,
        double subTotal
) {
}