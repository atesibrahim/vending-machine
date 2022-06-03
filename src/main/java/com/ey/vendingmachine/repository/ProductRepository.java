package com.ey.vendingmachine.repository;

import com.ey.vendingmachine.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>{
}
