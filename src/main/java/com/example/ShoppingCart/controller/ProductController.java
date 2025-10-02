package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.pojo.dto.ResponseMessage;
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

    //Query all products.
    @GetMapping("/products")
    public ResponseMessage<List<Product>>getAllProducts() {
        List<Product> products=pservice.getAllProducts();
        return ResponseMessage.success(products);
    }

    // Query a single product based on the product ID
    @GetMapping("/products/{productId}")
    public ResponseMessage<Product> getProductById(@PathVariable String productId) {
        Product product= pservice.getProductById(productId);
        return ResponseMessage.success(product);
    }

    // Perform a fuzzy search based on the product name
    @GetMapping("/products/search")
    public ResponseMessage<List<Product>> searchProductsByName(@RequestParam String productName) {
        List<Product> products=pservice.searchProductsByName(productName);
        return ResponseMessage.success(products);
    }
}
