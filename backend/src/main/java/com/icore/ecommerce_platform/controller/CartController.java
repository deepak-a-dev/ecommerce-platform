package com.icore.ecommerce_platform.controller;

import com.icore.ecommerce_platform.dto.AddToCartRequestDto;
import com.icore.ecommerce_platform.dto.CartResponseDto;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.icore.ecommerce_platform.dto.UpdateCartItemRequestDto;
import com.icore.ecommerce_platform.dto.OrderResponseDto;
import org.springframework.http.HttpStatus;

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

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponseDto> updateQuantity(@AuthenticationPrincipal User user,
                                                          @PathVariable int productId,
                                                          @Valid @RequestBody UpdateCartItemRequestDto request) {
        return ResponseEntity.ok(cartService.updateQuantity(user, productId, request.quantity()));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDto> removeItem(@AuthenticationPrincipal User user,
                                                      @PathVariable int productId) {
        return ResponseEntity.ok(cartService.removeItem(user, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDto> checkout(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.checkout(user));
    }
}