package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.SaleHistoryInterface;
import com.example.ShoppingCart.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/SaleHistory")
public class SaleHistoryController {

    @Autowired
    private SaleHistoryInterface SaleHistoryService;

    @GetMapping("/menu") //历史菜单
    public String getUserOrderHistory(HttpSession session, Model model) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        List<Order> orders = SaleHistoryService.getOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "order-history"; // 对应templates/order-history.html
    }

    @GetMapping("/menu/{orderId}/detail") //order detail
    public String viewOrderDetail(@PathVariable String orderId, Model model, HttpSession session) {
        // 获取订单项和支付信息
        List<OrderItem> orderItems = SaleHistoryService.getOrderItemsByOrderId(orderId);
        PaymentRecord payment = SaleHistoryService.getPaymentRecordByOrderId(orderId);

        // 添加到模型中
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("payment", payment);
        model.addAttribute("orderId", orderId);

        return "order-detail"; // 对应templates/order-detail.html
    }

    @GetMapping("/menu/items/{orderItemId}/product") //通过order detail找product
    public String viewOrderItemProduct(@PathVariable String orderItemId, Model model) {
        Product product = SaleHistoryService.getProductByOrderItemId(orderItemId);

        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST);
        }

        model.addAttribute("product", product);
        return "product-detail"; // 对应templates/product-detail.html
    }
}

