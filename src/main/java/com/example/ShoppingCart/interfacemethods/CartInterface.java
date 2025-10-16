package com.example.ShoppingCart.interfacemethods;

import java.util.List;

import com.example.ShoppingCart.model.CartRecord;

/**
 * Cart Service Interface - Defines all cart-related operations
 * This interface abstracts cart business logic from the controller layer
 */
public interface CartInterface {
    // Check if a specific product exists in user's cart
    CartRecord checkCartItem(String userId, String productId);

    // Update quantity with stock validation (for add/increase operations)
    CartRecord updateQuantity(CartRecord existingCartRecord, Integer quantity);

    // Update quantity without stock check (used for decrease operation)
    CartRecord updateQuantityWithoutStockCheck(CartRecord existingCartRecord, Integer quantity);

    // Create a new cart item for the user
    CartRecord createCartItem(String userId, String productId, Integer quantity);

    // Retrieve all cart items for a specific user
    List<CartRecord> getCartItemsByUserId(String userId);

    // Verify if product stock is sufficient for requested quantity
    boolean checkInventory(String productId, int quantity);

    // Get current available stock for a product
    int getProductInventory(String productId);
}
