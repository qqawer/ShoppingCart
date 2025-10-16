package com.example.ShoppingCart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ShoppingCart.model.User;

/**
 * User Repository - Data access layer for user operations
 * Provides custom query methods for user authentication and lookup
 */
@Repository
public interface UserRepository extends JpaRepository<User,String> {
    /**
     * Find user by username (used for checking username availability)
     */
    User findByUserName(String userName);

    /**
     * Find user by phone number (primary login credential)
     */
    User findByPhoneNumber(String phoneNumber);
}
