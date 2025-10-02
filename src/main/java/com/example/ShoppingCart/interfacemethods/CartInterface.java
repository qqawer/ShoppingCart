package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.CartRecord;

import java.util.List;

public interface CartInterface {
    //updateCartRecord
    public CartRecord updateCartItem(String userId, Long productId, Integer quantity);

    //updateCartItem
    public List<CartRecord> updateCartItemsByUserId(String userId, Long productId, Integer quantity);
}
