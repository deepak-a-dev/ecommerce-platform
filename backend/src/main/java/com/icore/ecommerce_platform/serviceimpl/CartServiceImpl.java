package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.CartItemRepository;
import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.dto.AddToCartRequestDto;
import com.icore.ecommerce_platform.dto.CartItemResponseDto;
import com.icore.ecommerce_platform.dto.CartResponseDto;
import com.icore.ecommerce_platform.entity.CartItem;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.exception.InsufficientStockException;
import com.icore.ecommerce_platform.exception.ResourceNotFoundException;
import com.icore.ecommerce_platform.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartResponseDto addToCart(User user, AddToCartRequestDto request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + request.productId() + " does not exist"));

        CartItem cartItem = cartItemRepository
                .findByUser_UserIdAndProduct_ProductId(user.getUserId(), product.getProductId())
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setUser(user);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    return ci;
                });

        int newQuantity = cartItem.getQuantity() + request.quantity();
        if (newQuantity > product.getStock()) {
            throw new InsufficientStockException(
                    "Insufficient stock for '" + product.getProductName()
                            + "': requested " + newQuantity + ", available " + product.getStock());
        }
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);

        return getCart(user);
    }

    @Override
    public CartResponseDto getCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser_UserId(user.getUserId());

        List<CartItemResponseDto> itemDtos = items.stream()
                .map(ci -> new CartItemResponseDto(
                        ci.getProduct().getProductId(),
                        ci.getProduct().getProductName(),
                        ci.getProduct().getProductPrice(),
                        ci.getQuantity(),
                        ci.getQuantity() * ci.getProduct().getProductPrice()))
                .toList();

        int totalItems = itemDtos.stream().mapToInt(CartItemResponseDto::quantity).sum();
        double grandTotal = itemDtos.stream().mapToDouble(CartItemResponseDto::subTotal).sum();

        return new CartResponseDto(itemDtos, totalItems, grandTotal);
    }
}