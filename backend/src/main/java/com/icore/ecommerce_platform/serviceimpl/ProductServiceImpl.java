package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import com.icore.ecommerce_platform.exception.ResourceNotFoundException;

/**
 * Default implementation of {@link ProductService}.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> addProduct(List<Product> productList) {
        return productRepository.saveAll(productList);
    }

    @Override
    @Transactional
    public void removeProduct(int productId) {
        Product existing = productRepository.getProductById(productId);
        if (existing == null) {
            throw new ResourceNotFoundException("Product with id " + productId + " does not exist");
        }
        productRepository.removeProductById(productId);
    }

    @Override
    public Product updateProduct(int id, Map<String, Object> fields) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " does not exist"));

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Product.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, existingProduct, value);
        });
        return productRepository.save(existingProduct);
    }
}
