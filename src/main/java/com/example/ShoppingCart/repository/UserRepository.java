package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {


}
