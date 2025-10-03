package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartInterface cartInterface;
    //add product into Cart
    @PostMapping("/add")
    public ResponseEntity<ResponseMessage<CartRecord>> addToCart(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam Integer quantity) {

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);

            if (existingCartRecord != null) {
                CartRecord updatedCart = cartInterface.updateQuantity(existingCartRecord, quantity);

                if (updatedCart == null) {
                    ResponseMessage<CartRecord> response = new ResponseMessage<>(
                            HttpStatus.OK.value(),
                            "商品已从购物车移除",
                            null
                    );
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.ok(ResponseMessage.success(updatedCart));
                }
            } else {
                CartRecord newCart = cartInterface.createCartItem(userId, productId, quantity);
                return ResponseEntity.ok(ResponseMessage.success(newCart));
            }

        } catch (IllegalArgumentException e) {
            ResponseMessage<CartRecord> response = new ResponseMessage<>(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseMessage<CartRecord> response = new ResponseMessage<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "服务器错误: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    //get user's Cart
    @GetMapping("/items")
    public ResponseEntity<ResponseMessage<List<CartRecord>>> getCartItems(@RequestParam String userId) {
        try {
            List<CartRecord> items = cartInterface.getCartItemsByUserId(userId);
            return ResponseEntity.ok(ResponseMessage.success(items));
        } catch (IllegalArgumentException e) {
            ResponseMessage<List<CartRecord>> response = new ResponseMessage<>(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseMessage<List<CartRecord>> response = new ResponseMessage<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "服务器错误: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    //delete the product of Cart
    @DeleteMapping("/remove")
    public ResponseEntity<ResponseMessage<String>> removeFromCart(
            @RequestParam String userId,
            @RequestParam String productId) {

        try {
            CartRecord existingCartRecord = cartInterface.checkCartItem(userId, productId);

            if (existingCartRecord == null) {
                ResponseMessage<String> response = new ResponseMessage<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "购物车中没有该商品",
                        null
                );
                return ResponseEntity.badRequest().body(response);
            }

            cartInterface.updateQuantity(existingCartRecord, 0);
            ResponseMessage<String> response = new ResponseMessage<>(
                    HttpStatus.OK.value(),
                    "商品已从购物车移除",
                    "删除成功"
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ResponseMessage<String> response = new ResponseMessage<>(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ResponseMessage<String> response = new ResponseMessage<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "服务器错误: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
