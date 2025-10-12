package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress,String> {
    List<UserAddress> findByUser_UserId(String userId);
    UserAddress findByUser_UserIdAndIsDefaultTrue(String userId);
}
