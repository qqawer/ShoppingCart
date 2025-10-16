package com.example.ShoppingCart.interfacemethods;

import org.springframework.validation.BindingResult;

import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.RegisterRequest;
import com.example.ShoppingCart.pojo.dto.UpdateUserRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;

import jakarta.servlet.http.HttpSession;

/**
 * User Service Interface - Defines all user-related operations
 * Handles authentication, session management, and user profile operations
 */
public interface UserInterface {
    /**
     * User login - Authenticate user and create session
     */
    UserInfoDTO login(LoginRequest request, HttpSession session, BindingResult bindingResult);

    /**
     * User logout - Invalidate session
     */
    void logout(HttpSession session);

    /**
     * Get current logged in user information from session
     */
    UserInfoDTO getCurrentUser(HttpSession session);

    /**
     * Check if user is currently logged in
     */
    boolean isLoggedIn(HttpSession session);

    /**
     * User registration - Create new user account
     */
    UserInfoDTO register(RegisterRequest request);

    /**
     * Update user information (profile, password, addresses)
     */
    UserInfoDTO updateUserInfo(String userId, UpdateUserRequest request);
}

