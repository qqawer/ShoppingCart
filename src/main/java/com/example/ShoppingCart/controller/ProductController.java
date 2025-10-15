package com.example.ShoppingCart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.interfacemethods.ProductInterface;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.utils.SortUtil;

import jakarta.servlet.http.HttpSession;

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

        // get the quantity of products in the cart
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
        // keep the quantity of products in the cart displayed
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);

        // handle the sorting logic
        Pageable sortedPageable = SortUtil.handleSorting(sort, pageable);
        String sortMessage = SortUtil.getSortMessage(sort);

        Page<Product> products;
        if (productName == null || productName.isEmpty()) {
            // display all products
            products = pservice.getAllProductsByStatus(sortedPageable);

            // if there is a sorting operation, display the sorting message
            if (sort != null && !sort.isEmpty()) {
                model.addAttribute("successMessage", "the products have been sorted by " + sortMessage);
            } else {
                model.addAttribute("errorMessage", "please enter the product name!");
            }
        }
        else {
            productName = productName.strip();
            products = pservice.searchAvailableProductsByName(productName, sortedPageable);

            if (products.isEmpty()) {
                model.addAttribute("errorMessage", "sorry, no products related to '" + productName + "' were found");
                // display all products
                products = pservice.getAllProductsByStatus(sortedPageable);
            }
            else {
                String message = "found " + products.getTotalElements() + " related products";
                // if there is a sorting operation, add the sorting information
                if (sort != null && !sort.isEmpty()) {
                    message += ", and sorted by " + sortMessage;
                }
                model.addAttribute("successMessage", message);
            }
        }

        model.addAttribute("page", products);
        model.addAttribute("searchKeyword", productName);
        model.addAttribute("currentSort", sort); // used to display the current sorting status in the front end

        return "product/lists";
    }

}
