package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * Business operations for the product catalog: creation, removal, and updates.
 */
public interface ProductService {

    String addProduct(Product product);

    String addProduct(List<Product> productList);

    String removeProduct(int productId);

    String updateProduct(int id, Map<String, Object> fields);


}
