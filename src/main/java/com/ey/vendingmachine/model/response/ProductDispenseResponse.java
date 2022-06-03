package com.ey.vendingmachine.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDispenseResponse {
    private String responseMessage;
    private String productName;
    private Double currentBalance;
}
