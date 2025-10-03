package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.CartRecord;

import java.util.List;

public interface CartInterface {
    //check whether product in Cart
    CartRecord checkCartItem(String userId, String productId);
    //update the quantity of product in Cart
    CartRecord updateQuantity(CartRecord existingCartRecord, Integer quantity);
    //create a new product in Cart
    CartRecord createCartItem(String userId, String productId, Integer quantity);
    //get all item in Cart
    List<CartRecord> getCartItemsByUserId(String userId);
}
