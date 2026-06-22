package com.icore.ecommerce_platform.controller;

import com.icore.ecommerce_platform.dao.OrderRepository;
import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.dto.OrderFormDto;
import com.icore.ecommerce_platform.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.icore.ecommerce_platform.dto.OrderResponseDto;
import jakarta.validation.Valid;
import com.icore.ecommerce_platform.dto.OrderSummaryDto;
import com.icore.ecommerce_platform.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

/**
 * REST endpoint for placing customer orders under {@code /api/order}.
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    OrderService orderService;
    OrderRepository orderRepository;
    UserRepository userRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository, UserRepository userRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDto> placeOrder(@AuthenticationPrincipal User user,
                                                       @Valid @RequestBody OrderFormDto orderFormDto) {
        OrderResponseDto response = orderService.placeOrder(user.getUsername(), orderFormDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderSummaryDto>> getOrderHistory(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrderHistory(user.getUsername()));
    }
}
