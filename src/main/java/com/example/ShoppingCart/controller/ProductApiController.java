package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.pojo.dto.ProductCreateDTO;
import com.example.ShoppingCart.pojo.dto.ProductUpdateDTO;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductApiController {
    @Autowired
    private ProductInterface pservice;
    @GetMapping("/api/products/lists")
    @ResponseBody
    public ResponseMessage<Page<Product>> getApiProduct(@PageableDefault(size = 12) Pageable pageable){
        Page<Product> products=pservice.getAllProducts(pageable);
        if (products.isEmpty()) {
            throw new BusinessException(ErrorCode.PRODUCT_LIST_EMPTY);
        }
        return ResponseMessage.success(products);
    }

    @GetMapping("/api/products/{productId}")
    @ResponseBody
    public ResponseMessage<Product> getApiProductById(@PathVariable String productId) {
        Product product= pservice.getProductById(productId);
        if (product==null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST);
        }
        return ResponseMessage.success(product);
    }

    @PostMapping("/api/addProduct")
    @ResponseBody
    public ResponseMessage<Product> createProduct( @Valid @RequestBody ProductCreateDTO createDTO) {
        Product createdProduct = pservice.createProduct(createDTO);
        return ResponseMessage.success(createdProduct);
    }
    @PostMapping("/api/updateProduct")
    @ResponseBody
    public ResponseMessage<Product> editProduct( @Valid @RequestBody ProductUpdateDTO updateDTO) {
        Product createdProduct = pservice.updateProduct(updateDTO);
        return ResponseMessage.success(createdProduct);
    }
    @PostMapping("/api/deleteProduct/{productId}")
    @ResponseBody
    public ResponseMessage<String> deleteProduct(@PathVariable String productId) {
        if (productId == null || productId.isBlank()) {
            throw new BusinessException( ErrorCode.PARAM_CANNOT_BE_NULL,"productId can not be null");
        }
        productId=productId.trim();
        pservice.deleteProduct(productId);
        return ResponseMessage.success(productId);
    }

}
