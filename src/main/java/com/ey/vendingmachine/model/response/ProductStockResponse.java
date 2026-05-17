package com.ey.vendingmachine.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductStockResponse {

    private Integer productId;
    private String productName;
    private Integer stock;
}
