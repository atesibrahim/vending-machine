package com.ey.vendingmachine.service.product;

import com.ey.vendingmachine.model.response.ProductDispenseResponse;
import com.ey.vendingmachine.model.request.ProductRequest;

public interface ProductService {

    ProductDispenseResponse dispense(ProductRequest request);
}
