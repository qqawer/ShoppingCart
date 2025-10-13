package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.pojo.dto.ProductCreateDTO;
import com.example.ShoppingCart.pojo.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface ProductInterface {
    // Query all products
    Page<Product> getAllProducts(Pageable pageable);

    //Query a single product based on the product ID.
    Product getProductById(String productId);

    //Query products based on the name
    Page<Product> searchProductsByName(String productName,Pageable pageable);

    Product createProduct(ProductCreateDTO product);

    Page<Product> getAllProductsByStatus(Pageable pageable);

    Page<Product> searchAvailableProductsByName(String productName, Pageable pageable);

    Product updateProduct(ProductUpdateDTO product);

    void deleteProduct(String productId);

    void updateStockAfterPayment(Order order);
}
