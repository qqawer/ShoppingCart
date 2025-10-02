package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductInterface pservice;

    // 查询所有商品
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return pservice.getAllProducts();
    }

    // 根据商品 ID 查询单个商品
    @GetMapping("/products/{productId}")
    public Product getProductById(@PathVariable String productId) {
        return pservice.getProductById(productId);
    }

    // 根据商品名称模糊查询
    @GetMapping("/products/search")
    public List<Product> searchProductsByName(@RequestParam String productName) {
        return pservice.searchProductsByName(productName);
    }
}
