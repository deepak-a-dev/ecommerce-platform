package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.OrderFormDto;

public interface OrderService {

    String placeOrder(OrderFormDto orderFormDto);
}
