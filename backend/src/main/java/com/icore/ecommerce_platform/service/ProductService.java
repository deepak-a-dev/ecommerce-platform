package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * Business operations for the product catalog: creation, removal, and updates.
 */
public interface ProductService {

    Product addProduct(Product product);

    List<Product> addProduct(List<Product> productList);

    void removeProduct(int productId);

    Product updateProduct(int id, Map<String, Object> fields);


}
