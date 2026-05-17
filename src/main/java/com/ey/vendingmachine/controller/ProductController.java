package com.ey.vendingmachine.controller;

import com.ey.vendingmachine.model.response.ProductDispenseResponse;
import com.ey.vendingmachine.model.response.ProductListResponse;
import com.ey.vendingmachine.model.response.ProductStockResponse;
import com.ey.vendingmachine.model.request.ProductRequest;

import java.util.List;
import com.ey.vendingmachine.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductListResponse> listAll(@RequestParam(defaultValue = "0") int page) {
        return productService.listAll(page);
    }

    @PostMapping
    public ProductDispenseResponse dispense(@Valid @RequestBody ProductRequest productRequest) {
        return productService.dispense(productRequest);
    }

    @GetMapping("/{id}/stock")
    public ProductStockResponse getStock(@PathVariable Integer id) {
        return productService.getStock(id);
    }
}