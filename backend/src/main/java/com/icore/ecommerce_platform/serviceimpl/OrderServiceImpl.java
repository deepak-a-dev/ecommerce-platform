package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.OrderItemRepository;
import com.icore.ecommerce_platform.dao.OrderRepository;
import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.dto.OrderFormDto;
import com.icore.ecommerce_platform.dto.ProductAvailabilityDto;
import com.icore.ecommerce_platform.dto.ProductNameQtyDto;
import com.icore.ecommerce_platform.entity.Order;
import com.icore.ecommerce_platform.entity.OrderItem;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.icore.ecommerce_platform.exception.ResourceNotFoundException;
import com.icore.ecommerce_platform.dto.OrderResponseDto;
import com.icore.ecommerce_platform.dto.OrderItemResponseDto;
import jakarta.transaction.Transactional;
import com.icore.ecommerce_platform.exception.InsufficientStockException;

/**
 * Default implementation of {@link OrderService}. Builds an order from the
 * submitted product list, persisting each line item and the order total.
 */
@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public OrderResponseDto placeOrder(OrderFormDto orderFormDto) {
        User user = userRepository.usernameVerification(orderFormDto.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User '" + orderFormDto.getUsername() + "' not found");
        }

        Order order = new Order();
        List<OrderItem> cart = new ArrayList<>();
        double total = 0.0;

        for (ProductNameQtyDto item : orderFormDto.getProductList()) {
            ProductAvailabilityDto availability = productRepository.verifyProductAvailability(item.getProductName());
            if (availability == null) {
                throw new ResourceNotFoundException(
                        "Product '" + item.getProductName() + "' is currently unavailable");
            }

            Product product = productRepository.getProductByProductName(item.getProductName());

            if (product.getStock() < item.getQty()) {
                throw new InsufficientStockException(
                        "Insufficient stock for '" + product.getProductName()
                                + "': requested " + item.getQty() + ", available " + product.getStock());
            }
            product.setStock(product.getStock() - item.getQty());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setProductName(product.getProductName());
            orderItem.setUnitPrice(product.getProductPrice());
            orderItem.setQty(item.getQty());
            orderItem.setSubTotal(item.getQty() * product.getProductPrice());
            orderItemRepository.save(orderItem);
            cart.add(orderItem);
            total += item.getQty() * product.getProductPrice();
        }

        order.setDateOfOrder(LocalDateTime.now());
        order.setOrderItemList(cart);
        order.setTotal(total);
        order.setUser(user);
        orderRepository.save(order);

        List<OrderItemResponseDto> items = cart.stream()
                .map(oi -> new OrderItemResponseDto(
                        oi.getProductName(), oi.getQty(), oi.getUnitPrice(), oi.getSubTotal()))
                .toList();

        return new OrderResponseDto(
                order.getOrderId(), user.getUsername(), order.getDateOfOrder(), total, items);
    }
}
