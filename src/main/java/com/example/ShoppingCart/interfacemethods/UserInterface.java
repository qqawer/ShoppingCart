package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.BindingResult;

public interface UserInterface {
    /**
     * 用户登录
     */
    UserInfoDTO login(LoginRequest request, HttpSession session, BindingResult bindingResult);

    /**
     * 用户登出
     */
    void logout(HttpSession session);

    /**
     * 获取当前登录用户信息
     */
    UserInfoDTO getCurrentUser(HttpSession session);

    /**
     * 检查用户是否已登录
     */
    boolean isLoggedIn(HttpSession session);
}

