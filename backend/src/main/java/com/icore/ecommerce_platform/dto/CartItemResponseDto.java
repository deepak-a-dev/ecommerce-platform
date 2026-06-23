package com.icore.ecommerce_platform.dto;

public record CartItemResponseDto(
        int productId,
        String productName,
        double unitPrice,
        int quantity,
        double subTotal
) {
}