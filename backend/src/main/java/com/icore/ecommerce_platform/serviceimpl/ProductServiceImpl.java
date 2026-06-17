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

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String addProduct(Product product) {
        productRepository.save(product);
        return "PRODUCT ADDED SUCCESSFULLY";
    }

    @Override
    public String addProduct(List<Product> productList) {
        productRepository.saveAll(productList);
        return "LIST OF PRODUCTS ADDED SUCCESSFULLY";
    }

    @Override
    @Transactional
    public String removeProduct(int productId) {
        Product temp = productRepository.getProductById(productId);
        if (temp == null) {
            return "Product with id -> " + productId + " doesn't exist";
        } else {
            productRepository.removeProductById(productId);
            return "Product with id -> " + productId + " removed successfully";
        }

    }

    @Override
    public String updateProduct(int id, Map<String, Object> fields) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("RECORD NOT FOUND"));

        if (existingProduct != null) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Product.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingProduct, value);
            });
            productRepository.save(existingProduct);

        }
        return "Product Updated...";
    }
}
