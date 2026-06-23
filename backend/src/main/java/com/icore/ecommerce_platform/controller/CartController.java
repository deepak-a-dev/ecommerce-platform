package com.icore.ecommerce_platform.controller;

import com.icore.ecommerce_platform.dto.AddToCartRequestDto;
import com.icore.ecommerce_platform.dto.CartResponseDto;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for the authenticated user's shopping cart under {@code /api/cart}.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addToCart(@AuthenticationPrincipal User user,
                                                     @Valid @RequestBody AddToCartRequestDto request) {
        return ResponseEntity.ok(cartService.addToCart(user, request));
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.getCart(user));
    }
}