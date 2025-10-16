package com.example.ShoppingCart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.CartInterface;
import com.example.ShoppingCart.interfacemethods.SaleHistoryInterface;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.SessionConstant;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/SaleHistory")
public class SaleHistoryController {

    @Autowired
    private SaleHistoryInterface SaleHistoryService;

    @Autowired
    private CartInterface cartInterface;

    @GetMapping("/menu") //history menu
    public String getUserOrderHistory(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,HttpSession session, Model model) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderTime"));
        Page<Order> OrderRecords = SaleHistoryService.getUserOrders(userId, pageable);
        model.addAttribute("orders", OrderRecords);
        System.out.println("pages:" + OrderRecords.getTotalPages());
        int cartCount = 0;
        if (userId != null) {
            cartCount = cartInterface.getCartItemsByUserId(userId).size();
        }
        model.addAttribute("cartCount", cartCount);

        return "SaleHistory/order-history"; // corresponding SaleHistory/order-history.html
    }

    @GetMapping("/menu/{orderId}/detail") //order detail
    public String viewOrderDetail(@PathVariable String orderId, Model model, HttpSession session) {
        // get the order items and payment information
        List<OrderItem> orderItems = SaleHistoryService.getOrderItemsByOrderId(orderId);
        PaymentRecord payment = SaleHistoryService.getPaymentRecordByOrderId(orderId);

        // add to the model
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("payment", payment);
        model.addAttribute("orderId", orderId);

        return "SaleHistory/order-detail"; // corresponding SaleHistory/order-detail.html
    }

    @GetMapping("/products/{orderItemId}") //find the product through the order detail
    public String viewOrderItemProduct(@PathVariable String orderItemId, Model model) {
        Product product = SaleHistoryService.getProductByOrderItemId(orderItemId);

        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST);
        }

        model.addAttribute("product", product);
        return "product/product-detail"; // corresponding product-detail.html
    }
}

