package com.example.ShoppingCart.interfacemethods;

import org.springframework.validation.BindingResult;

import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.RegisterRequest;
import com.example.ShoppingCart.pojo.dto.UpdateUserRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;

import jakarta.servlet.http.HttpSession;

public interface UserInterface {
    /**
     * user login
     */
    UserInfoDTO login(LoginRequest request, HttpSession session, BindingResult bindingResult);

    /**
     * user logout
     */
    void logout(HttpSession session);

    /**
     * get the current logged in user information
     */
    UserInfoDTO getCurrentUser(HttpSession session);

    /**
     * check if the user is logged in
     */
    boolean isLoggedIn(HttpSession session);

    /**
     * user register
     */
    UserInfoDTO register(RegisterRequest request);

    /**
     * update user information
     */
    UserInfoDTO updateUserInfo(String userId, UpdateUserRequest request);
}

