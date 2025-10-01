package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.ProductInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductInterface pservice;
}
