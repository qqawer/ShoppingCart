package com.example.ShoppingCart.service;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductImplementation implements ProductInterface {
    @Autowired
    private ProductRepository prepo;

    @Override
    @Transactional
    public Page<Product> getAllProducts(Pageable pageable) {

        return prepo.findAll(pageable);
    }

    @Override
    @Transactional
    public Product getProductById(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL);
        }
        return prepo.findByProductId(productId.trim());
    }

    @Override
    @Transactional
    public Page<Product> searchProductsByName(String productName,Pageable pageable) {
        return prepo.findByProductNameContaining(productName,pageable);
    }


}
