package com.example.ShoppingCart.controller;

import org.springframework.ui.Model;
import com.example.ShoppingCart.interfacemethods.OrderInterface;

import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        User user = (User)session.getAttribute("user");
        String userId = user.getUserId();
        Order order= oservice.createOrder(userId);
        session.setAttribute("order",order);
        return "redirect:/order/confirm";
    }


@GetMapping("/order/confirm")
    public String confirm(Model model,HttpSession session){
    //补全拦截器

    Order order =(Order) session.getAttribute("order");
    model.addAttribute("currentPendingOrder",order);
    return "confirm-page";                  //需要填支付方式

    }

@PostMapping("/order/payment")
    public String payment(HttpSession session,String paymentMethod){
    User user = (User)session.getAttribute("user");
    Order order =oservice.findPendingOrder(user.getUserId());

    oservice.createPaymentRecord(paymentMethod,order);

    return "redirect:/payment/success";         //点击支付跳转支付成功
                                                //生成订单记录
}

@GetMapping("/order/payment/success")
    public String paymentSuccess(HttpSession session,Model model){
    User user = (User)session.getAttribute("user");
    Order order =oservice.findPaidOrder(user.getUserId());
    model.addAttribute("currentPaidOrder",order);
    return "payment-success";
}
}
