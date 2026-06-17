package com.icore.ecommerce_platform.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderFormDto {

    private String username;
    private List<ProductNameQtyDto> productList;
}
