package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface UserAddressRepository extends JpaRepository<UserAddress,String> {
    UserAddress findByUser_UserId(String userId);
}
