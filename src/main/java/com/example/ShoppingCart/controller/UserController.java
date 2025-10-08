package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserInterface userService;

    /**
     * 显示登录页面
     * GET /login
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "product/login";
    }

    /**
     * 用户登录
     * POST /login
     */
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "输入信息有误，请检查");
            return "product/login";
        }

        try {
            UserInfoDTO userInfo = userService.login(request, session, bindingResult);
            // 登录成功，重定向到商品列表页面
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "product/login";
        }
    }

    /**
     * 用户登出
     * GET /logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/login";
    }

    /**
     * 显示用户信息页面
     * GET /user/profile
     */
    @GetMapping("/user/profile")
    public String getUserProfile(HttpSession session, Model model) {
        try {
            UserInfoDTO userInfo = userService.getCurrentUser(session);
            model.addAttribute("userInfo", userInfo);
            return "product/profile";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
}
