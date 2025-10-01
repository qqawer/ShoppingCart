package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserInterface uservice;
}
