package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.AddToCartRequestDto;
import com.icore.ecommerce_platform.dto.CartResponseDto;
import com.icore.ecommerce_platform.entity.User;

public interface CartService {
    CartResponseDto addToCart(User user, AddToCartRequestDto request);
    CartResponseDto getCart(User user);
}