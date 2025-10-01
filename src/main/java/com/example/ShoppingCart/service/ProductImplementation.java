package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.ProductInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductImplementation implements ProductInterface {
    @Autowired
    private ProductImplementation prepo;
}
