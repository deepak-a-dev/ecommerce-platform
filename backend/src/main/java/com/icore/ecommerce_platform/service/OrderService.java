package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.OrderFormDto;
import com.icore.ecommerce_platform.dto.OrderResponseDto;

/**
 * Business operations for customer orders.
 */
public interface OrderService {

    OrderResponseDto placeOrder(OrderFormDto orderFormDto);
}
