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
    public String placeOrder(OrderFormDto orderFormDto) {


        User user = userRepository.usernameVerification(orderFormDto.getUsername());
        double total;
        Order order = new Order();
        List<OrderItem> cart = new ArrayList<>();
        total = 0.0;
        List<ProductNameQtyDto> list = orderFormDto.getProductList();
        for (ProductNameQtyDto item : list) {
            ProductAvailabilityDto productAvailabilityDto = productRepository.verifyProductAvailability(item.getProductName());
            if (productAvailabilityDto == null) {
                return "Sorry! Looks like " + item.getProductName() + " is currently unavailable";
            } else {
                Product product = productRepository.getProductByProductName(item.getProductName());
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setOrder(order);
                orderItem.setProductName(product.getProductName());
                orderItem.setUnitPrice(product.getProductPrice());
                orderItem.setQty(item.getQty());
                orderItem.setSubTotal(item.getQty() * product.getProductPrice());
                orderItemRepository.save(orderItem);
                cart.add(orderItem);
                total = total + item.getQty() * product.getProductPrice();
            }
        }
        order.setDateOfOrder(LocalDateTime.now());
        order.setOrderItemList(cart);
        order.setTotal(total);
        order.setUser(user);
        orderRepository.save(order);

        return null;
    }
}
