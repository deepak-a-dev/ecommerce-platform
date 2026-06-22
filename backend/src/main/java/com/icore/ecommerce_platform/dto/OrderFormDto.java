package com.icore.ecommerce_platform.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Order must contain at least one product")
    @Valid
    private List<ProductNameQtyDto> productList;
}
