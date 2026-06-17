package com.icore.ecommerce_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductAvailabilityDto {

    private int productId;
    private String productName;
    private String productCategory;
    private double productPrice;
    private boolean productStatus;
}
