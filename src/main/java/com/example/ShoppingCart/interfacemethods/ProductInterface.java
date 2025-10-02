package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductInterface {
    // Query all products
     Page<Product> getAllProducts(Pageable pageable);

    //Query a single product based on the product ID.
    Product getProductById(String productId);

    //Query products based on the name
    Page<Product> searchProductsByName(String productName,Pageable pageable);
}
