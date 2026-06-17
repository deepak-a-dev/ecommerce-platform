package com.icore.ecommerce_platform.dao;

import com.icore.ecommerce_platform.dto.ProductAvailabilityDto;
import com.icore.ecommerce_platform.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Product getProductById(int productId);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.productId = :productId")
    void removeProductById(int productId);

    @Query("SELECT new com.icore.ecommerce_platform.dto.ProductAvailabilityDto(p.productId, p.productName, p.productCategory, p.productPrice, p.productStatus) FROM Product p WHERE p.productName=:productName AND p.productStatus=true")
    ProductAvailabilityDto verifyProductAvailability(String productName);

    @Query("SELECT p FROM Product p WHERE p.productName=:productName")
    Product getProductByProductName(String productName);

    @Query("SELECT DISTINCT productCategory FROM Product")
    List<String> getCategoryList();

    @Query("SELECT p FROM Product p")
    List<Product> getAllProducts();

    @Query("SELECT p FROM Product p WHERE p.productCategory = :name")
    List<Product> basedOnCategory(String name);
}
