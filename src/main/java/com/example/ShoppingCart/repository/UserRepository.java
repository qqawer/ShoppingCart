package com.example.ShoppingCart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ShoppingCart.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    /**
     * find user by user name (for login)
     */
    User findByUserName(String userName);

    /**
     * find user by phone number (for login)
     */
    User findByPhoneNumber(String phoneNumber);
}
