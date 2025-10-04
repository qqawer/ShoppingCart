package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.repository.CartRepository;
import com.example.ShoppingCart.repository.ProductRepository;
import com.example.ShoppingCart.repository.UserRepository;
import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Achieve CartInterface
@Service
@Transactional
public class CartImplementation implements CartInterface {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    //Check whether the product in the Cart
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
    //Update the quantity of product in Cart
    @Override
    public CartRecord updateQuantity(CartRecord existingCartRecord, Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "数量不能为负数");
        }

        if (quantity == 0) {
            cartRepository.delete(existingCartRecord);
            return null;
        }

        existingCartRecord.setQuantity(existingCartRecord.getQuantity() + quantity);
        return cartRepository.save(existingCartRecord);
    }
    //Create the product in Cart
    @Override
    public CartRecord createCartItem(String userId, String productId, Integer quantity) {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "userId");
        }
        if (productId == null || productId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "productId");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "数量必须大于0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_EXIST));

        CartRecord newCartRecord = new CartRecord();
        newCartRecord.setUser(user);
        newCartRecord.setProduct(product);
        newCartRecord.setQuantity(quantity);
        newCartRecord.setSelected(true);

        return cartRepository.save(newCartRecord);
    }
    //Get all the product in Cart
    @Override
    public List<CartRecord> getCartItemsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "userId");
        }
        return cartRepository.findByUser_UserId(userId);
    }

}
