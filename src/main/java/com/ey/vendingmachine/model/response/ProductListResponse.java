package com.ey.vendingmachine.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductListResponse {

    private Integer id;
    private String name;
    private Double price;
    private Integer stock;
}
