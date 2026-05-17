package com.ey.vendingmachine.service.product;

import com.ey.vendingmachine.domain.Product;
import com.ey.vendingmachine.model.response.ProductDispenseResponse;
import com.ey.vendingmachine.model.response.ProductListResponse;
import com.ey.vendingmachine.model.response.ProductStockResponse;
import com.ey.vendingmachine.model.request.ProductRequest;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import com.ey.vendingmachine.repository.ProductRepository;
import com.ey.vendingmachine.data.CoinAmount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CoinAmount coinAmount;

    @Override
    public ProductDispenseResponse dispense(ProductRequest request) {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + request.getId()));

        if (product.getStock() < 1) {
            throw new IllegalStateException("Product is sold out: " + product.getName());
        }

        double balance = coinAmount.getBalance() != null ? coinAmount.getBalance() : 0.0;

        if (balance < product.getPrice()) {
            throw new IllegalStateException(
                    "Insufficient balance: required " + product.getPrice() + " but current balance is " + balance);
        }

        updateStock(product);
        coinAmount.setBalance(balance - product.getPrice());
        return ProductDispenseResponse.builder()
                .productName(product.getName())
                .responseMessage("THANK YOU")
                .currentBalance(coinAmount.getBalance())
                .build();
    }

    @Override
    public ProductStockResponse getStock(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        return ProductStockResponse.builder()
                .productId(product.getId())
                .productName(product.getName())
                .stock(product.getStock())
                .build();
    }

    @Override
    public List<ProductListResponse> listAll(int page) {
        return productRepository.findAll(PageRequest.of(page, 15)).stream()
                .map(product -> ProductListResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .build())
                .collect(Collectors.toList());
    }

    private void updateStock(Product product) {
        product.setStock(product.getStock()-1);
        productRepository.save(product);
    }
}
