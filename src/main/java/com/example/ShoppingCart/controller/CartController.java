package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.SessionConstant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartInterface cartInterface;

    // 显示购物车页面
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            // 如果未登录，使用测试用户ID
            userId = "testUser123";
        }

        List<CartRecord> cartItems = cartInterface.getCartItemsByUserId(userId);

        // 计算总计（所有商品）
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartRecord item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(new BigDecimal(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("userId", userId);

        return "product/cart";
    }

    // 增加商品数量
    @GetMapping("/cart/increase")
    public String increaseQuantity(@RequestParam String productId, HttpSession session, RedirectAttributes redirectAttributes) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            return "redirect:/login";
        }

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
            if (existingCartRecord != null) {
                cartInterface.updateQuantity(existingCartRecord, 1);
            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }

    // 减少商品数量
    @GetMapping("/cart/decrease")
    public String decreaseQuantity(@RequestParam String productId, HttpSession session) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            return "redirect:/login";
        }

        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if ((existingCartRecord != null) && (existingCartRecord.getQuantity() != 1)) {
            cartInterface.updateQuantity(existingCartRecord, -1);
        }
        return "redirect:/cart";
    }

    // 删除商品
    @GetMapping("/cart/remove")
    public String removeItem(@RequestParam String productId, HttpSession session) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            return "redirect:/login";
        }

        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if (existingCartRecord != null) {
            // 传递负数使数量变为0或以下，触发删除逻辑
            cartInterface.updateQuantity(existingCartRecord, -existingCartRecord.getQuantity());
        }
        return "redirect:/cart";
    }

    //add product into Cart
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam String productId,
            @RequestParam(required = false, defaultValue = "1") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String userId = (String) session.getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            // 如果未登录，重定向到登录页面
            return "redirect:/login";
        }

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);

            if (existingCartRecord != null) {
                cartInterface.updateQuantity(existingCartRecord, quantity);
            } else {
                cartInterface.createCartItem(userId, productId, quantity);
            }
            redirectAttributes.addFlashAttribute("successMessage", "商品已成功加入购物车");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/products/lists";
    }
}