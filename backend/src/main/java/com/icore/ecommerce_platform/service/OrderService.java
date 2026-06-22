package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.OrderFormDto;
import com.icore.ecommerce_platform.dto.OrderResponseDto;
import com.icore.ecommerce_platform.dto.OrderSummaryDto;
import java.util.List;

/**
 * Business operations for customer orders.
 */
public interface OrderService {

    OrderResponseDto placeOrder(OrderFormDto orderFormDto);

    List<OrderSummaryDto> getOrderHistory(String username);
}
