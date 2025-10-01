package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CartImplementation implements CartInterface {
    @Autowired
    private CartRepository prepo;
}
