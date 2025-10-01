package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserImplementation implements UserInterface {
    @Autowired
    private UserRepository prepo;
}
