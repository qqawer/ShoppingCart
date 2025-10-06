package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        // 计算总计
        BigDecimal totalPrice = BigDecimal.ZERO;
        int selectedCount = 0;
        for (CartRecord item : cartItems) {
            if (item.getSelected()) {
                selectedCount++;
                BigDecimal itemTotal = item.getProduct().getPrice()
                        .multiply(new BigDecimal(item.getQuantity()));
                totalPrice = totalPrice.add(itemTotal);
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("selectedCount", selectedCount);
        model.addAttribute("userId", userId);

        return "product/cart";
    }

    // 增加商品数量
    @GetMapping("/cart/increase")
    public String increaseQuantity(@RequestParam String userId, @RequestParam String productId) {
        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if (existingCartRecord != null) {
            cartInterface.updateQuantity(existingCartRecord, existingCartRecord.getQuantity() + 1);
        }
        return "redirect:/cart";
    }

    // 减少商品数量
    @GetMapping("/cart/decrease")
    public String decreaseQuantity(@RequestParam String userId, @RequestParam String productId) {
        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);
        if (existingCartRecord != null) {
            int newQuantity = existingCartRecord.getQuantity() - 1;
            cartInterface.updateQuantity(existingCartRecord, newQuantity);
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
    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseMessage<CartRecord> addToCart(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam Integer quantity) {

        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);

        if (existingCartRecord != null) {
            CartRecord updatedCart = cartInterface.updateQuantity(existingCartRecord, quantity);

            if (updatedCart == null) {
                return new ResponseMessage<>(
                        HttpStatus.OK.value(),
                        "商品已从购物车移除",
                        null
                );
            } else {
                return ResponseMessage.success(updatedCart);
            }
        } else {
            CartRecord newCart = cartInterface.createCartItem(userId, productId, quantity);
            return ResponseMessage.success(newCart);
        }
    }
    //get user's Cart
    @GetMapping("/api/cart/items")
    @ResponseBody
    public ResponseMessage<List<CartRecord>> getCartItems(@RequestParam String userId) {
        List<CartRecord> items = cartInterface.getCartItemsByUserId(userId);
        return ResponseMessage.success(items);
    }
    //delete the product of Cart
    @DeleteMapping("/api/cart/remove")
    @ResponseBody
    public ResponseMessage<String> removeFromCart(
            @RequestParam String userId,
            @RequestParam String productId) {

        CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);

        if (existingCartRecord == null) {
            return new ResponseMessage<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "购物车中没有该商品",
                    null
            );
        }

        cartInterface.updateQuantity(existingCartRecord, 0);
        return new ResponseMessage<>(
                HttpStatus.OK.value(),
                "商品已从购物车移除",
                "删除成功"
        );
    }
}
