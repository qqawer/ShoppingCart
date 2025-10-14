package com.example.ShoppingCart.interfacemethods;

import java.util.List;

import com.example.ShoppingCart.model.CartRecord;

public interface CartInterface {
    //check whether product in Cart
    CartRecord checkCartItem(String userId, String productId);
    //update the quantity of product in Cart
    CartRecord updateQuantity(CartRecord existingCartRecord, Integer quantity);
    //update the quantity without stock check (for decrease operation)
    CartRecord updateQuantityWithoutStockCheck(CartRecord existingCartRecord, Integer quantity);
    //create a new product in Cart
    CartRecord createCartItem(String userId, String productId, Integer quantity);
    //get all item in Cart
    List<CartRecord> getCartItemsByUserId(String userId);
    //check inventory whether it is enough
    boolean checkInventory(String productId, int quantity);
    //get recent inventory amount
    int getProductInventory(String productId);
}
