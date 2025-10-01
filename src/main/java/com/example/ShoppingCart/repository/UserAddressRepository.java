package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress,Integer> {
}
