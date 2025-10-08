package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.RegisterRequest;
import com.example.ShoppingCart.pojo.dto.UpdateUserRequest;
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
            userService.login(request, session, bindingResult);
            // 登录成功，重定向到商品列表页面
            return "redirect:/products/lists";
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

    /**
     * 显示注册页面
     * GET /register
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "product/register";
    }

    /**
     * 用户注册
     * POST /register
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "输入信息有误，请检查");
            return "product/register";
        }

        try {
            userService.register(request);
            model.addAttribute("success", "注册成功，请登录");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "product/register";
        }
    }

    /**
     * 显示编辑用户信息页面
     * GET /user/edit
     */
    @GetMapping("/user/edit")
    public String editProfilePage(HttpSession session, Model model) {
        try {
            UserInfoDTO userInfo = userService.getCurrentUser(session);
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("updateRequest", new UpdateUserRequest());
            return "product/edit-profile";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    /**
     * 更新用户信息
     * POST /user/update
     */
    @PostMapping("/user/update")
    public String updateProfile(@Valid @ModelAttribute UpdateUserRequest request,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "输入信息有误，请检查");
            return "product/edit-profile";
        }

        try {
            UserInfoDTO currentUser = userService.getCurrentUser(session);
            UserInfoDTO updatedUser = userService.updateUserInfo(currentUser.getUserId(), request);
            // 更新 Session 中的用户信息
            session.setAttribute(SessionConstant.CURRENT_USER, updatedUser);
            model.addAttribute("success", "信息更新成功");
            return "redirect:/user/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "product/edit-profile";
        }
    }
}
