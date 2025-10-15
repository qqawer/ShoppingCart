package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.pojo.dto.ProductCreateDTO;
import com.example.ShoppingCart.pojo.dto.ProductUpdateDTO;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.utils.SortUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @Autowired
    private ProductInterface pservice;

    @Autowired
    private CartInterface cartInterface;

    //Query all products.
    @GetMapping("products/lists")
    public String getAllProducts(@PageableDefault(size = 12, sort = "price") Pageable pageable,
                                 Model model, HttpSession session) {
        Page<Product> products=pservice.getAllProductsByStatus(pageable);
        model.addAttribute("page", products);

        // 获取购物车商品数量
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);

        return "product/lists";
    }



    // Query a single product based on the product ID
    @GetMapping("/products/{productId}")
    public String getProductById(@PathVariable String productId,Model model,HttpSession session) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_CANNOT_BE_NULL,"productId can not be null");
        }
        Product product= pservice.getProductById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST, productId);
        }
        model.addAttribute("product",product);
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);
        return "product/product-detail";
    }

    @GetMapping("/products/search")
    public String searchProductsByName(@RequestParam(required = false) String productName,
                                       @RequestParam(required = false) String sort,
                                       @PageableDefault(size = 12) Pageable pageable,
                                       Model model,
                                       HttpSession session) {
        // 保持购物车数量显示
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);

        // 处理排序逻辑
        Pageable sortedPageable = SortUtil.handleSorting(sort, pageable);
        String sortMessage = SortUtil.getSortMessage(sort);

        Page<Product> products;
        if (productName == null || productName.isEmpty()) {
            // 显示所有商品
            products = pservice.getAllProductsByStatus(sortedPageable);

            // 如果有排序操作，显示排序消息
            if (sort != null && !sort.isEmpty()) {
                model.addAttribute("successMessage", "商品已按" + sortMessage + "排列");
            } else {
                model.addAttribute("errorMessage", "请输入商品名称！");
            }
        }
        else {
            productName = productName.strip();
            products = pservice.searchAvailableProductsByName(productName, sortedPageable);

            if (products.isEmpty()) {
                model.addAttribute("errorMessage", "抱歉，没有找到与 '" + productName + "' 相关的商品");
                // 显示所有商品
                products = pservice.getAllProductsByStatus(sortedPageable);
            }
            else {
                String message = "找到 " + products.getTotalElements() + " 个相关商品";
                // 如果有排序，添加排序信息
                if (sort != null && !sort.isEmpty()) {
                    message += "，并按" + sortMessage + "排列";
                }
                model.addAttribute("successMessage", message);
            }
        }

        model.addAttribute("page", products);
        model.addAttribute("searchKeyword", productName);
        model.addAttribute("currentSort", sort); // 用于前端显示当前排序状态

        return "product/lists";
    }

}
