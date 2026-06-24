package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.AddToCartRequestDto;
import com.icore.ecommerce_platform.dto.CartResponseDto;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.dto.OrderResponseDto;

public interface CartService {

    CartResponseDto addToCart(User user, AddToCartRequestDto request);

    CartResponseDto getCart(User user);

    CartResponseDto updateQuantity(User user, int productId, int quantity);

    CartResponseDto removeItem(User user, int productId);

    void clearCart(User user);

    OrderResponseDto checkout(User user);
}