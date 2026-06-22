package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.OrderItemRepository;
import com.icore.ecommerce_platform.dao.OrderRepository;
import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.dto.OrderFormDto;
import com.icore.ecommerce_platform.dto.OrderResponseDto;
import com.icore.ecommerce_platform.dto.ProductAvailabilityDto;
import com.icore.ecommerce_platform.dto.ProductNameQtyDto;
import com.icore.ecommerce_platform.entity.Order;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.exception.InsufficientStockException;
import com.icore.ecommerce_platform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks private OrderServiceImpl orderService;

    // small helper to build a one-item order form
    private OrderFormDto orderFor(String productName, int qty) {
        OrderFormDto form = new OrderFormDto();
        form.setProductList(List.of(new ProductNameQtyDto(productName, qty)));
        return form;
    }

    @Test
    void placeOrder_throwsWhenUserNotFound() {
        when(userRepository.usernameVerification("ghost")).thenReturn(null);

        assertThatThrownBy(() -> orderService.placeOrder("ghost", orderFor("Widget", 1)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ghost");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_throwsWhenProductUnavailable() {
        when(userRepository.usernameVerification("admin")).thenReturn(new User());
        when(productRepository.verifyProductAvailability("Ghost")).thenReturn(null);

        assertThatThrownBy(() -> orderService.placeOrder("admin", orderFor("Ghost", 1)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ghost");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_throwsWhenInsufficientStock() {
        Product widget = new Product();
        widget.setProductName("Widget");
        widget.setProductPrice(100.0);
        widget.setStock(1);

        when(userRepository.usernameVerification("admin")).thenReturn(new User());
        when(productRepository.verifyProductAvailability("Widget"))
                .thenReturn(new ProductAvailabilityDto(1, "Widget", "Misc", 100.0, true));
        when(productRepository.getProductByProductName("Widget")).thenReturn(widget);

        assertThatThrownBy(() -> orderService.placeOrder("admin", orderFor("Widget", 5)))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Widget");

        assertThat(widget.getStock()).isEqualTo(1);     // NOT decremented
        verify(orderRepository, never()).save(any());   // order NOT saved
    }

    @Test
    void placeOrder_decrementsStock_calculatesTotal_andReturnsDto() {
        User user = new User();
        user.setUsername("admin");

        Product widget = new Product();
        widget.setProductName("Widget");
        widget.setProductPrice(100.0);
        widget.setStock(5);

        when(userRepository.usernameVerification("admin")).thenReturn(user);
        when(productRepository.verifyProductAvailability("Widget"))
                .thenReturn(new ProductAvailabilityDto(1, "Widget", "Misc", 100.0, true));
        when(productRepository.getProductByProductName("Widget")).thenReturn(widget);

        OrderResponseDto result = orderService.placeOrder("admin", orderFor("Widget", 2));

        // the returned DTO
        assertThat(result.total()).isEqualTo(200.0);          // 2 x 100
        assertThat(result.username()).isEqualTo("admin");
        assertThat(result.items()).hasSize(1);
        assertThat(result.items().get(0).qty()).isEqualTo(2);
        assertThat(result.items().get(0).subTotal()).isEqualTo(200.0);

        // the side effects
        assertThat(widget.getStock()).isEqualTo(3);           // 5 - 2 decremented
        verify(productRepository).save(widget);               // decrement persisted
        verify(orderRepository).save(any(Order.class));       // order persisted
    }
}