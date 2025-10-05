package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserInterface userService;

    /**
     * 显示登录页面
     * GET /login
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 返回 login.html 模板
    }

    /**
     * 用户登录
     * POST /api/user/login
     */
    @PostMapping("/api/user/login")
    @ResponseBody
    public ResponseMessage<UserInfoDTO> login(
            @RequestBody LoginRequest request,
            HttpSession session) {
        UserInfoDTO userInfo = userService.login(request, session);
        return ResponseMessage.success(userInfo);
    }

    /**
     * 用户登出
     * POST /api/user/logout
     */
    @PostMapping("/api/user/logout")
    @ResponseBody
    public ResponseMessage<Void> logout(HttpSession session) {
        userService.logout(session);
        return new ResponseMessage<>(200, "登出成功", null);
    }

    /**
     * 获取当前登录用户信息
     * GET /api/user/me
     */
    @GetMapping("/api/user/me")
    @ResponseBody
    public ResponseMessage<UserInfoDTO> getCurrentUser(HttpSession session) {
        UserInfoDTO userInfo = userService.getCurrentUser(session);
        return ResponseMessage.success(userInfo);
    }

    /**
     * 检查登录状态
     * GET /api/user/check
     */
    @GetMapping("/api/user/check")
    @ResponseBody
    public ResponseMessage<Boolean> checkLogin(HttpSession session) {
        boolean isLoggedIn = userService.isLoggedIn(session);
        return ResponseMessage.success(isLoggedIn);
    }
}
