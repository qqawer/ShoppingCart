package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.model.*;
import org.springframework.ui.Model;
import com.example.ShoppingCart.interfacemethods.OrderInterface;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class OrderController {
    @Autowired
    private OrderInterface orderService;

    /**
     * 创建订单
     * POST /checkout/order
     */
    //用拦截器去判断是否登录
    //不用重复的session判断
    //补全拦截器后删除
    //添加html
    //点击checkout 跳转订单确认页面
    @PostMapping("/order")
    public String createOrder(HttpSession session) {
        // 使用统一的 Session 常量获取用户 ID
        //补全拦截器
        //生成订单和订单项目
        //需要添加异常处理
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        Order order = orderService.createOrder(userId);
        session.setAttribute("order", order);
        return "redirect:/checkout/order/confirm";
    }

    /**
     * 订单确认页面
     * GET /checkout/order/confirm
     */
    @GetMapping("/order/confirm")
    public String confirm(Model model, HttpSession session) {
        //补全拦截器
        //session 存order
        Order order = (Order) session.getAttribute("order");
        model.addAttribute("currentPendingOrder", order);
        return "confirm-page";
    }
    @PostMapping("/order/cancel")
    public String cancelOrder(HttpSession session) {
        Order order = (Order) session.getAttribute("order");
        orderService.cancelOrder(order);
        session.removeAttribute("order");
        return "redirect:/product/lists";
    }

    /**
     * 处理支付
     * POST /checkout/order/payment
     */
    @PostMapping("/order/payment")
    public String payment(HttpSession session, @RequestParam String paymentMethod) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        Order order = orderService.findPendingOrder(userId);
        orderService.createPaymentRecord(paymentMethod, order);
        return "redirect:/checkout/order/payment/success";
    }

    /**
     * 支付成功页面
     * GET /checkout/order/payment/success
     */
    @GetMapping("/order/payment/success")
    public String paymentSuccess(HttpSession session, Model model) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        Order order = orderService.findPaidOrder(userId);
        model.addAttribute("currentPaidOrder", order);
        return "payment-success";
    }




}
