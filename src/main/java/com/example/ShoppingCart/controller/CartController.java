package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartInterface cartInterface;
    //add product into Cart
    @PostMapping("/add")
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
    @GetMapping("/items")
    public ResponseMessage<List<CartRecord>> getCartItems(@RequestParam String userId) {
        List<CartRecord> items = cartInterface.getCartItemsByUserId(userId);
        return ResponseMessage.success(items);
    }
    //delete the product of Cart
    @DeleteMapping("/remove")
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
