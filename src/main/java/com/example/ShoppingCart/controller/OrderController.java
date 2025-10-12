package com.example.ShoppingCart.controller;

import com.example.ShoppingCart.model.*;
import com.example.ShoppingCart.repository.UserAddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import com.example.ShoppingCart.interfacemethods.OrderInterface;

import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/checkout")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderInterface orderService;
    
    @Autowired
    private UserAddressRepository userAddressRepository;

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
        session.setAttribute("orderId", order.getOrderId());
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
        String orderId = (String) session.getAttribute("orderId");
        if (orderId == null) {
            log.warn("orderId is null in session, redirecting to products list");
            return "redirect:/products/lists";
        }
        Order order = orderService.findByOrderId(orderId);
        
        // 获取用户的所有地址
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        List<UserAddress> userAddresses = userAddressRepository.findByUser_UserId(userId);
        
        if(userAddresses == null || userAddresses.isEmpty()) {
            log.warn("No addresses found for user, showing error");
            model.addAttribute("error", "请先填写收货地址。");
        }
        
        model.addAttribute("userAddresses", userAddresses);
        model.addAttribute("currentPendingOrder", order);
        return "confirm-page";
    }

    @PostMapping("/order/cancel")
    public String cancelOrder(HttpSession session) {
        try {
            String orderId = (String) session.getAttribute("orderId");
            if (orderId == null) {
                log.warn("orderId is null when canceling order");
                return "redirect:/products/lists";
            }
            log.info("Canceling order: {}", orderId);
            Order order = orderService.findByOrderId(orderId);
            orderService.cancelOrder(order);
            session.removeAttribute("orderId");
            return "redirect:/products/lists";
        } catch (Exception e) {
            log.error("Error canceling order", e);
            throw e;
        }
    }

    /**
     * 处理支付
     * POST /checkout/order/payment
     */
    @PostMapping("/order/payment")
    public String payment(HttpSession session, 
                         @RequestParam(required = false) String paymentMethod,
                         @RequestParam(required = false) String selectedAddressId,
                         Model model) {
        try {
            String orderId = (String) session.getAttribute("orderId");
            if (orderId == null) {
                log.warn("orderId is null when processing payment");
                return "redirect:/products/lists";
            }

            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                log.warn("paymentMethod is null or empty");
                model.addAttribute("error", "Please select a payment method");
                Order order = orderService.findByOrderId(orderId);
                model.addAttribute("currentPendingOrder", order);
                return "confirm-page";
            }
            
            if (selectedAddressId == null || selectedAddressId.trim().isEmpty()) {
                log.warn("selectedAddressId is null or empty");
                model.addAttribute("error", "Please select a delivery address");
                Order order = orderService.findByOrderId(orderId);
                
                // 重新获取用户地址列表
                String userId = (String) session.getAttribute(SessionConstant.USER_ID);
                List<UserAddress> userAddresses = userAddressRepository.findByUser_UserId(userId);
                model.addAttribute("userAddresses", userAddresses);
                model.addAttribute("currentPendingOrder", order);
                return "confirm-page";
            }

            log.info("Processing payment for order: {}, method: {}, address: {}", orderId, paymentMethod, selectedAddressId);
            Order order = orderService.findByOrderId(orderId);
            
            // 设置选中的地址
            UserAddress selectedAddress = userAddressRepository.findById(selectedAddressId).orElse(null);
            if (selectedAddress != null) {
                order.setAddress(selectedAddress);
                orderService.updateOrder(order);
            }
            
            orderService.createPaymentRecord(paymentMethod, order);
            // 支付成功后移除 session 中的 orderId
            session.removeAttribute("orderId");
            return "redirect:/checkout/order/payment/success";
        } catch (Exception e) {
            log.error("Error processing payment", e);
            // 出错时不抛出异常，而是重定向到商品列表
            session.removeAttribute("orderId");
            return "redirect:/products/lists";
        }
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
