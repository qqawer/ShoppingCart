package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,String> {
    Product findByProductId(String productId); // 返回实体而非 Optional
    List<Product> findByProductNameContaining(String productName); // 模糊查询
}
