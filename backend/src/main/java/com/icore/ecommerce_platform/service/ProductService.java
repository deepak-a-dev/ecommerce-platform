package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    String addProduct(Product product);

    String addProduct(List<Product> productList);

    String removeProduct(int productId);

//    String updateProduct(Integer productId, String productName, String brandName, String productCategory);

    String updateProduct(int id, Map<String, Object> fields);


}
