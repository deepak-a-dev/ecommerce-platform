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
import com.icore.ecommerce_platform.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import jakarta.transaction.Transactional;
import com.icore.ecommerce_platform.dto.OrderFormDto;
import com.icore.ecommerce_platform.dto.OrderResponseDto;
import com.icore.ecommerce_platform.dto.ProductNameQtyDto;
import com.icore.ecommerce_platform.exception.InvalidRequestException;


@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           OrderService orderService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
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

    @Override
    public CartResponseDto updateQuantity(User user, int productId, int quantity) {
        CartItem cartItem = cartItemRepository
                .findByUser_UserIdAndProduct_ProductId(user.getUserId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + productId + " is not in the cart"));

        if (quantity > cartItem.getProduct().getStock()) {
            throw new InsufficientStockException(
                    "Insufficient stock for '" + cartItem.getProduct().getProductName()
                            + "': requested " + quantity + ", available " + cartItem.getProduct().getStock());
        }
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return getCart(user);
    }

    @Override
    public CartResponseDto removeItem(User user, int productId) {
        CartItem cartItem = cartItemRepository
                .findByUser_UserIdAndProduct_ProductId(user.getUserId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + productId + " is not in the cart"));
        cartItemRepository.delete(cartItem);
        return getCart(user);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser_UserId(user.getUserId());
    }

    @Override
    @Transactional
    public OrderResponseDto checkout(User user) {
        List<CartItem> cartItems = cartItemRepository.findByUser_UserId(user.getUserId());
        if (cartItems.isEmpty()) {
            throw new InvalidRequestException("Cart is empty");
        }

        // turn the cart into the productList the order service understands
        List<ProductNameQtyDto> productList = cartItems.stream()
                .map(ci -> new ProductNameQtyDto(ci.getProduct().getProductName(), ci.getQuantity()))
                .toList();

        OrderFormDto orderForm = new OrderFormDto();
        orderForm.setProductList(productList);

        // reuse the existing order logic (stock check + decrement + order record)
        OrderResponseDto order = orderService.placeOrder(user.getUsername(), orderForm);

        // success → empty the cart
        cartItemRepository.deleteByUser_UserId(user.getUserId());
        return order;
    }
}