package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.SessionConstant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String increaseQuantity(@RequestParam String userId, @RequestParam String productId) {
        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if (existingCartRecord != null) {
            cartInterface.updateQuantity(existingCartRecord, 1);
        }
        return "redirect:/cart";
    }

    // 减少商品数量
    @GetMapping("/cart/decrease")
    public String decreaseQuantity(@RequestParam String userId, @RequestParam String productId) {
        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if (existingCartRecord != null) {
            cartInterface.updateQuantity(existingCartRecord, -1);
        }
        return "redirect:/cart";
    }

    // 删除商品
    @GetMapping("/cart/remove")
    public String removeItem(@RequestParam String userId, @RequestParam String productId) {
        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if (existingCartRecord != null) {
            cartInterface.updateQuantity(existingCartRecord, 0);
        }
        return "redirect:/cart";
    }

    //add product into Cart
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam Integer quantity) {

        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);

        if (existingCartRecord != null) {
            cartInterface.updateQuantity(existingCartRecord, quantity);
        } else {
            cartInterface.createCartItem(userId, productId, quantity);
        }

        return "redirect:/cart";
    }
}