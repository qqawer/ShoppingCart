package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductImplementation implements ProductInterface {
    @Autowired
    private ProductRepository prepo;

    @Override
    @Transactional
    public List<Product> getAllProducts() {
        return prepo.findAll();
    }

    @Override
    @Transactional
    public Product getProductById(String productId) {
        return prepo.findByProductId(productId);
    }

    @Override
    @Transactional
    public List<Product> searchProductsByName(String productName) {
        return prepo.findByProductNameContaining(productName);
    }


}
