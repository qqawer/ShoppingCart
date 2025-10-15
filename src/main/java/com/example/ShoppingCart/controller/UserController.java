package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.model.UserAddress;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.RegisterRequest;
import com.example.ShoppingCart.pojo.dto.UpdateUserRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import com.example.ShoppingCart.repository.UserAddressRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserInterface userService;
    
    @Autowired
    private UserAddressRepository userAddressRepository;
    
    /**
     * 删除地址
     * POST /user/address/delete/{addressId}
     */
    @PostMapping("/user/address/delete/{addressId}")
    public String deleteAddress(@PathVariable String addressId, HttpSession session, Model model, 
                               RedirectAttributes redirectAttributes) {
        // 检查用户是否登录
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }
        
        try {
            // 获取要删除的地址
            UserAddress address = userAddressRepository.findById(addressId).orElse(null);
            
            // 检查地址是否存在且属于当前用户
            if (address != null && address.getUser().getUserId().equals(userId)) {
                userAddressRepository.delete(address);
                redirectAttributes.addFlashAttribute("successMessage", "地址删除成功");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "地址不存在或不属于当前用户");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "删除地址失败: " + e.getMessage());
        }
        
        return "redirect:/user/addresses";
    }

    /**
     * 显示登录页面
     * GET /login
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "user/login";
    }
    
    /**
     * 显示用户地址列表页面
     * GET /user/addresses
     */
    @GetMapping("/user/addresses")
    public String addressesPage(HttpSession session, Model model) {
        // 检查用户是否登录
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        if (userId == null) {
            return "redirect:/login";
        }

        // 获取用户所有地址
        List<UserAddress> addresses = userAddressRepository.findByUser_UserId(userId);
        model.addAttribute("addresses", addresses);
        
        return "user/addresses";
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
            return "user/login";
        }

        try {
            UserInfoDTO userInfo = userService.login(request, session, bindingResult);
            // 根据用户角色跳转不同页面
            if ("ADMIN".equals(userInfo.getRole())) {
                // 管理员跳转到百度
                return "redirect:/#/products";
            } else {
                // 顾客跳转到商品列表页面
                return "redirect:/products/lists";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user/login";
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
            return "user/profile";
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
        return "user/register";
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
            return "user/register";
        }

        try {
            userService.register(request);
            model.addAttribute("success", "注册成功，请登录");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "user/register";
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
            return "user/edit-profile";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    /**
     * 更新用户信息
     * POST /user/update
     */
    @PostMapping("/user/update")
    public String updateProfile(@Valid @ModelAttribute("updateRequest") UpdateUserRequest request,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model) {
        try {
            UserInfoDTO currentUser = userService.getCurrentUser(session);
            model.addAttribute("userInfo", currentUser);  // 关键：重新放回

            if (bindingResult.hasErrors()) {
                model.addAttribute("error", "输入信息有误，请检查");
                model.addAttribute("updateRequest", request);
                return "user/edit-profile";
            }

            UserInfoDTO updatedUser = userService.updateUserInfo(currentUser.getUserId(), request);
            session.setAttribute(SessionConstant.CURRENT_USER, updatedUser);
            model.addAttribute("success", "信息更新成功");
            return "redirect:/user/profile";
        } catch (Exception e) {
            try {
                model.addAttribute("userInfo", userService.getCurrentUser(session));
            } catch (Exception ignored) {}
            model.addAttribute("updateRequest", request);
            model.addAttribute("error", e.getMessage());
            return "user/edit-profile";
        }
    }
    /**
     * 设置默认地址
     * POST /user/address/default/{addressId}
     */
    @PostMapping("/user/address/default/{addressId}")
    public String setDefaultAddress(@PathVariable String addressId, HttpSession session) {
        try {
            UserInfoDTO userInfo = userService.getCurrentUser(session);
            // 先将所有地址设为非默认
            List<UserAddress> addresses = userAddressRepository.findByUser_UserId(userInfo.getUserId());
            for (UserAddress address : addresses) {
                address.setDefault(false);
                userAddressRepository.save(address);
            }
            // 设置选中的地址为默认
            UserAddress selectedAddress = userAddressRepository.findById(addressId).orElse(null);
            if (selectedAddress != null) {
                selectedAddress.setDefault(true);
                userAddressRepository.save(selectedAddress);
            }
            
            return "redirect:/user/addresses";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
}
