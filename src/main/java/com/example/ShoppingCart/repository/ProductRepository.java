package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product>findAll(Pageable pageable);
    Product findByProductId(String productId); // 返回实体而非 Optional
    Page<Product> findByProductNameContaining(String productName,Pageable pageable); // 模糊查询


}
