package com.ey.vendingmachine.controller;

import com.ey.vendingmachine.model.reponse.ProductDispenseResponse;
import com.ey.vendingmachine.model.request.ProductRequest;
import com.ey.vendingmachine.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductDispenseResponse dispense(@Valid @RequestBody ProductRequest productRequest) {
        return productService.dispense(productRequest);
    }
}