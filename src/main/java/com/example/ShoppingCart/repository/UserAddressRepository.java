package com.example.ShoppingCart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ShoppingCart.model.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress,String> {
    List<UserAddress> findByUser_UserId(String userId);
    UserAddress findByUser_UserIdAndIsDefaultTrue(String userId);
}
