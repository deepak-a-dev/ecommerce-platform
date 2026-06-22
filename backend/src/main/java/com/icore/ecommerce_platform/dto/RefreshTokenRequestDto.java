package com.icore.ecommerce_platform.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(
        @NotBlank(message = "Refresh token is required") String refreshToken
) {
}