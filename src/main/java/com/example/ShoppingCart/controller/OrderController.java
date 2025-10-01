package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.OrderInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderInterface oservice;
}
