package com.ey.vendingmachine.service.product;

import com.ey.vendingmachine.model.response.ProductDispenseResponse;
import com.ey.vendingmachine.model.response.ProductListResponse;
import com.ey.vendingmachine.model.response.ProductStockResponse;
import com.ey.vendingmachine.model.request.ProductRequest;

import java.util.List;

public interface ProductService {

    ProductDispenseResponse dispense(ProductRequest request);

    ProductStockResponse getStock(Integer productId);

    List<ProductListResponse> listAll(int page);
}
