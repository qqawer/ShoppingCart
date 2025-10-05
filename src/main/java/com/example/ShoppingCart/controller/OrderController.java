package com.example.ShoppingCart.controller;

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
    private OrderInterface oservice;

    //用拦截器去判断是否登录
    //不用重复的session判断
    //补全拦截器后删除
    //添加html
//点击checkout 跳转订单确认页面
@PostMapping("/order")
    public String createOrder(HttpSession session){
    //补全拦截器
    //生成订单和订单项目
    //需要添加异常处理

        String userId = (String) session.getAttribute("userId");
        Order order= oservice.createOrder(userId);
        session.setAttribute("order",order);
        return "redirect:/order/confirm";
    }


@GetMapping("/order/confirm")
    public String confirm(Model model,HttpSession session){
    //补全拦截器
    //session 存order
    Order order =(Order) session.getAttribute("order");
    model.addAttribute("currentPendingOrder",order);
    return "confirm-page";                  //需要填支付方式

    }

@PostMapping("/order/payment")
    public String payment(HttpSession session,String paymentMethod){
    String userId = (String) session.getAttribute("userId");
    Order order =oservice.findPendingOrder(userId);

    oservice.createPaymentRecord(paymentMethod,order);

    return "redirect:/payment/success";         //点击支付跳转支付成功
                                                //生成订单记录
}

@GetMapping("/order/payment/success")
    public String paymentSuccess(HttpSession session,Model model){
    String userId = (String) session.getAttribute("userId");
    Order order =oservice.findPaidOrder(userId);
    model.addAttribute("currentPaidOrder",order);
    return "payment-success";
}

    @GetMapping("/order/history") //历史菜单
    public String getUserOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // 未登录时重定向到登录页面
        }

        List<Order> orders = oservice.getOrdersByUserId(user.getUserId());
        model.addAttribute("orders", orders);
        return "order-history"; // 对应templates/order-history.html
    }

    @GetMapping("/order/history/{orderId}/detail") //order detail
    public String viewOrderDetail(@PathVariable String orderId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // 获取订单项和支付信息
        List<OrderItem> orderItems = oservice.getOrderItemsByOrderId(orderId);
        PaymentRecord payment = oservice.getPaymentRecordByOrderId(orderId);

        // 添加到模型中
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("payment", payment);
        model.addAttribute("orderId", orderId);

        return "order-detail"; // 对应templates/order-detail.html
    }

    @GetMapping("/order/history/items/{orderItemId}/product") //通过order detail找product
    public String viewOrderItemProduct(@PathVariable String orderItemId, Model model) {
        Product product = oservice.getProductByOrderItemId(orderItemId);

        if (product == null) {
            return "error-page"; // 产品未找到
        }

        model.addAttribute("product", product);
        return "product-detail"; // 对应templates/product-detail.html
    }


}
