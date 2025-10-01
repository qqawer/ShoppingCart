package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.CartInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    @Autowired
    private CartInterface cservice;
}
