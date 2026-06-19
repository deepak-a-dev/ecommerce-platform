package com.icore.ecommerce_platform.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderFormDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotEmpty(message = "Order must contain at least one product")
    @Valid
    private List<ProductNameQtyDto> productList;
}
