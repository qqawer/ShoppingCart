package com.example.ShoppingCart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.repository.CartRepository;
import com.example.ShoppingCart.repository.ProductRepository;
import com.example.ShoppingCart.repository.UserRepository;

import jakarta.transaction.Transactional;

/**
 * Cart Service Implementation - Implements all cart business logic
 * Handles cart operations with validation, stock checking, and database updates
 */
@Service
@Transactional
public class CartImplementation implements CartInterface {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Check if product exists in user's cart
     * Logic: Validate parameters, query database, return cart record or null
     */
    @Override
    public CartRecord checkCartItem(String userId, String productId) {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "userId");
        }
        if (productId == null || productId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "productId");
        }

        Optional<CartRecord> cartRecord = cartRepository.findByUser_UserIdAndProduct_ProductId(userId, productId);
        return cartRecord.orElse(null);
    }

    /**
     * Update cart item quantity with stock validation
     * Logic: Calculate new quantity, delete if <=0, check stock, update database
     */
    @Override
    public CartRecord updateQuantity(CartRecord existingCartRecord, Integer quantity) {
        if (quantity == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "数量不能为空");
        }

        int newQuantity = existingCartRecord.getQuantity() + quantity;

        // if new quantity is less than or equal to 0, delete the cart item
        if (newQuantity <= 0) {
            cartRepository.delete(existingCartRecord);
            return null;
        }

        // check if the stock is enough
        Product product = existingCartRecord.getProduct();
        if (product.getStock() < newQuantity) {
            throw new BusinessException(ErrorCode.PRODUCT_STOCK_NOT_ENOUGH, product.getStock());
        }

        existingCartRecord.setQuantity(newQuantity);
        return cartRepository.save(existingCartRecord);
    }

    /**
     * Update quantity without stock validation (for decrease operation)
     * Logic: Calculate new quantity, delete if <=0, save without checking stock
     */
    @Override
    public CartRecord updateQuantityWithoutStockCheck(CartRecord existingCartRecord, Integer quantity) {
        if (quantity == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Quantity cannot be empty");
        }

        int newQuantity = existingCartRecord.getQuantity() + quantity;

        // if new quantity is less than or equal to 0, delete the cart item
        if (newQuantity <= 0) {
            cartRepository.delete(existingCartRecord);
            return null;
        }

        existingCartRecord.setQuantity(newQuantity);
        return cartRepository.save(existingCartRecord);
    }

    /**
     * Create new cart item for user
     * Logic: Validate input, check user/product exist, verify stock, create and save record
     */
    @Override
    public CartRecord createCartItem(String userId, String productId, Integer quantity) {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "userId");
        }
        if (productId == null || productId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "productId");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Quantity must be greater than 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_EXIST));

        // check if the stock is enough
        if (product.getStock() < quantity) {
            throw new BusinessException(ErrorCode.PRODUCT_STOCK_NOT_ENOUGH, product.getStock());
        }

        CartRecord newCartRecord = new CartRecord();
        newCartRecord.setUser(user);
        newCartRecord.setProduct(product);
        newCartRecord.setQuantity(quantity);
        newCartRecord.setSelected(true);

        return cartRepository.save(newCartRecord);
    }

    /**
     * Retrieve all cart items for a user
     * Logic: Validate userId, query all cart records from database
     */
    @Override
    public List<CartRecord> getCartItemsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "userId");
        }
        return cartRepository.findByUser_UserId(userId);
    }



    /**
     * Check if inventory is sufficient for requested quantity
     * Logic: Find product, compare stock with required quantity
     */
    @Override
    public boolean checkInventory(String productId, int required_quantity) {
        Product product = productRepository.findByProductId(productId);
        return product != null && product.getStock() >= required_quantity;
    }

    /**
     * Get current stock level for a product
     * Logic: Find product, return stock or 0 if not found
     */
    @Override
    public int getProductInventory(String productId) {
        Product product = productRepository.findByProductId(productId);
        return product != null ? product.getStock() : 0;
    }
}