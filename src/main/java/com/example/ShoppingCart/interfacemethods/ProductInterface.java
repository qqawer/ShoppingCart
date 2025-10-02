package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.Product;

import java.util.List;

public interface ProductInterface {
    // Query all products
    public List<Product> getAllProducts();

    //Query a single product based on the product ID.
    public Product getProductById(String productId);

    //Query products based on the name
    public List<Product> searchProductsByName(String productName);
}
