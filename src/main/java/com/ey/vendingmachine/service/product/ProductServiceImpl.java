package com.ey.vendingmachine.service.product;

import com.ey.vendingmachine.domain.Product;
import com.ey.vendingmachine.model.response.ProductDispenseResponse;
import com.ey.vendingmachine.model.request.ProductRequest;
import com.ey.vendingmachine.repository.ProductRepository;
import com.ey.vendingmachine.data.CoinAmount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CoinAmount coinAmount;

    @Override
    public ProductDispenseResponse dispense(ProductRequest request) {
        Optional<Product> product = productRepository.findById(request.getId());

        if (product.get().getStock() < 1) {
            return ProductDispenseResponse.builder()
                    .responseMessage("SOLD OUT")
                    .currentBalance(coinAmount.getBalance())
                    .build();
        }

        double balance = coinAmount.getBalance() != null ? coinAmount.getBalance() : 0.0;

        if (balance < product.get().getPrice()) {
            return ProductDispenseResponse.builder()
                    .responseMessage("Unfortunately your balance is not sufficient.")
                    .currentBalance(balance)
                    .build();
        }

        updateStock(product.get());
        coinAmount.setBalance(balance - product.get().getPrice());
        return ProductDispenseResponse.builder()
                .productName(product.get().getName())
                .responseMessage("THANK YOU")
                .currentBalance(coinAmount.getBalance())
                .build();
    }

    private void updateStock(Product product) {
        product.setStock(product.getStock()-1);
        productRepository.save(product);
    }
}
