package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.OrderFormDto;

/**
 * Business operations for customer orders.
 */
public interface OrderService {

    String placeOrder(OrderFormDto orderFormDto);
}
