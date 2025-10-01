package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.OrderInterface;
import com.example.ShoppingCart.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderImplementation implements OrderInterface {
    @Autowired
    private OrderRepository prepo;
}
