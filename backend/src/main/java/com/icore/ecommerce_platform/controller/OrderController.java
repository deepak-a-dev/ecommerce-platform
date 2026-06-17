package com.icore.ecommerce_platform.controller;

import com.icore.ecommerce_platform.dao.OrderRepository;
import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.dto.OrderFormDto;
import com.icore.ecommerce_platform.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String placeOrder(@RequestBody OrderFormDto orderFormDto) {
        return orderService.placeOrder(orderFormDto);
    }
}
