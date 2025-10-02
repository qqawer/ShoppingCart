package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductInterface pservice;

    //Query all products.
    @GetMapping("/products")
    public ResponseMessage<Page<Product>>getAllProducts(Pageable pageable) {
        Page<Product> products=pservice.getAllProducts(pageable);
//        if (products.isEmpty()) {
//            throw new BusinessException(ErrorCode.PRODUCT_LIST_EMPTY);
//        }
        return ResponseMessage.success(products);
    }

    // Query a single product based on the product ID
    @GetMapping("/products/{productId}")
    public ResponseMessage<Product> getProductById(@PathVariable String productId) {
        Product product= pservice.getProductById(productId);
        if (product==null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST);
        }
        return ResponseMessage.success(product);
    }

    // Perform a fuzzy search based on the product name
    @GetMapping("/products/search")
    public ResponseMessage<Page<Product>> searchProductsByName(@RequestParam(required = false) String productName,Pageable pageable) {
        if (productName == null || productName.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL);
        }
        productName = productName.strip();
        Page<Product> products=pservice.searchProductsByName(productName,pageable);

        return ResponseMessage.success(products);
    }
}
