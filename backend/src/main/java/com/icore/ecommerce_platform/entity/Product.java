package com.icore.ecommerce_platform.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "pro_name")
    private String productName;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "pro_category")
    private String productCategory;

    @Column(name = "pro_price")
    private double productPrice;

    @Column(name = "pro_status")
    private boolean productStatus;
}
