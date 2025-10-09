package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product>findAll(Pageable pageable);
    Product findByProductId(String productId); // 返回实体而非 Optional
    Page<Product> findByProductNameContaining(String productName,Pageable pageable); // 模糊查询

    void deleteProductByProductId(String productId);
    Page<Product> findByStatus(Integer status, Pageable pageable);
    boolean existsByProductName(String productName);
    @Query("SELECT p FROM Product p WHERE p.status = 1 AND p.stock > 0")
    Page<Product> findAvailableProducts(Pageable pageable);

}
