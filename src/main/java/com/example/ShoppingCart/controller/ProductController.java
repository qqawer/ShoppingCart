package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductInterface pservice;

    //Query all products.
    @GetMapping("/lists")
    public String getAllProducts(@PageableDefault(size = 12) Pageable pageable,
                                 HttpSession session, Model model) {
        Page<Product> products=pservice.getAllProducts(pageable);
//        if (products.isEmpty()) {
//            throw new BusinessException(ErrorCode.PRODUCT_LIST_EMPTY);
//        }

        // 获取当前登录用户ID，如果未登录则提示登录
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        model.addAttribute("userId", userId);
        model.addAttribute("page", products);
        return "product/lists";
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
