package com.icore.ecommerce_platform.dto;

import java.util.List;

public record CartResponseDto(
        List<CartItemResponseDto> items,
        int totalItems,
        double grandTotal
) {
}