package com.example.ShoppingCart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ShoppingCart.model.Product;


public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product>findAll(Pageable pageable);
    Product findByProductId(String productId); // return entity rather than Optional
    Page<Product> findByProductNameContaining(String productName,Pageable pageable); // fuzzy query

    void deleteProductByProductId(String productId);
    Page<Product> findByStatus(Integer status, Pageable pageable);
    boolean existsByProductName(String productName);
    @Query("SELECT p FROM Product p WHERE p.status = 1 AND p.stock > 0")
    Page<Product> findAvailableProducts(Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:productName% AND p.status = 1 AND p.stock > 0")
    Page<Product> findAvailableProductsByName(@Param("productName") String productName, Pageable pageable);

}
