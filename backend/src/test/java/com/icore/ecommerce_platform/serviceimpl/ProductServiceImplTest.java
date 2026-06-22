package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @InjectMocks private ProductServiceImpl productService;

    @Test
    void removeProduct_throwsWhenProductNotFound() {
        when(productRepository.getProductById(99)).thenReturn(null);

        assertThatThrownBy(() -> productService.removeProduct(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).removeProductById(anyInt());
    }

    @Test
    void removeProduct_deletesWhenProductExists() {
        when(productRepository.getProductById(1)).thenReturn(new Product());

        productService.removeProduct(1);

        verify(productRepository).removeProductById(1);
    }

    @Test
    void updateProduct_throwsWhenProductNotFound() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(99, Map.of("brandName", "Acme")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void addProduct_savesAndReturnsProduct() {
        Product product = new Product();
        product.setProductName("Widget");
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(product);

        assertThat(result.getProductName()).isEqualTo("Widget");
        verify(productRepository).save(product);
    }
}