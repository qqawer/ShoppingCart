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

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.SaleHistoryInterface;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.model.SessionConstant;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/SaleHistory")
public class SaleHistoryController {

    @Autowired
    private SaleHistoryInterface SaleHistoryService;

    @GetMapping("/menu") //历史菜单
    public String getUserOrderHistory(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,HttpSession session, Model model) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
//        System.out.println("Session userId: " + userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderTime"));
        Page<Order> OrderRecords = SaleHistoryService.getUserOrders(userId, pageable);
//        List<Order> orders = SaleHistoryService.getOrdersByUserId(userId);
        model.addAttribute("orders", OrderRecords);
        System.out.println("pages:" + OrderRecords.getTotalPages());
//        System.out.println("first:" + OrderRecords.stream().findFirst());
//        System.out.println("last:" + OrderRecords.nextOrLastPageable());
        return "SaleHistory/order-history"; // 对应SaleHistory/order-history.html
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

        return "SaleHistory/order-detail"; // 对应SaleHistory/order-detail.html
    }

    @GetMapping("/products/{orderItemId}") //通过order detail找product
    public String viewOrderItemProduct(@PathVariable String orderItemId, Model model) {
        Product product = SaleHistoryService.getProductByOrderItemId(orderItemId);

        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_EXIST);
        }

        model.addAttribute("product", product);
        return "product/product-detail"; // 对应product-detail.html
    }
}

