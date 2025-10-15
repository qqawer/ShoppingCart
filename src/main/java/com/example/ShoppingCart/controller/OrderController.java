package com.example.ShoppingCart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.ShoppingCart.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.interfacemethods.OrderInterface;
import com.example.ShoppingCart.repository.UserAddressRepository;
import com.example.ShoppingCart.service.AlipayImplementation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderInterface orderService;
    
    @Autowired
    private UserAddressRepository userAddressRepository;

    /**
     *  create the order
     * POST /checkout/order
     */
    //use the interceptor to check whether login
    //no need to repeat the session judgment
    //complete the interceptor and delete it
    //add html
    //click checkout to jump to the order confirmation page
    //click checkout to jump to the order confirmation page
    @PostMapping("/order")
    public String createOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        // use the unified Session constant to get the user ID
        //use the unified Session constant to get the user ID
        //complete the interceptor
        //generate the order and order item
        //need to add exception handling
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        try {
            Order order = orderService.createOrder(userId);
            session.setAttribute("orderId", order.getOrderId());
            return "redirect:/checkout/order/confirm";
        } catch (BusinessException e) {
            // if the order creation fails (like stock is not enough), return the cart page and display the error
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cart";
        }
    }

    /**
     * order confirmation page
     * GET /checkout/order/confirm
     */
    @GetMapping("/order/confirm")
    public String confirm(Model model, HttpSession session) {
        //complete the interceptor
        //session save order
        String orderId = (String) session.getAttribute("orderId");
        if (orderId == null) {
            log.warn("orderId is null in session, redirecting to products list");
            return "redirect:/products/lists";
        }
        Order order = orderService.findByOrderId(orderId);
        
        // get all the addresses of the user
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        List<UserAddress> userAddresses = userAddressRepository.findByUser_UserId(userId);
        
        if(userAddresses == null || userAddresses.isEmpty()) {
            log.warn("No addresses found for user, showing error");
            model.addAttribute("error", "please fill in the delivery address first.");
        }
        
        model.addAttribute("userAddresses", userAddresses);
        model.addAttribute("currentPendingOrder", order);
        return "payment/confirm-page";
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
     * handle the payment
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
                return "payment/confirm-page";
            }
            
            if (paymentMethod.equals("alipay")) {
                return "redirect:/Alipay?paymentMethod=alipay";
            }
            
            if (selectedAddressId == null || selectedAddressId.trim().isEmpty()) {
                log.warn("selectedAddressId is null or empty");
                model.addAttribute("error", "Please select a delivery address");
                Order order = orderService.findByOrderId(orderId);
                
                // re-get the user address list
                String userId = (String) session.getAttribute(SessionConstant.USER_ID);
                List<UserAddress> userAddresses = userAddressRepository.findByUser_UserId(userId);
                model.addAttribute("userAddresses", userAddresses);
                model.addAttribute("currentPendingOrder", order);
                return "payment/confirm-page";
            }

            log.info("Processing payment for order: {}, method: {}, address: {}", orderId, paymentMethod, selectedAddressId);
            Order order = orderService.findByOrderId(orderId);
            
            // set the selected address
            UserAddress selectedAddress = userAddressRepository.findById(selectedAddressId).orElse(null);
            if (selectedAddress != null) {
                order.setAddress(selectedAddress);
                orderService.updateOrder(order);
            }
            
            orderService.createPaymentRecord(paymentMethod, order);
                // after the payment is successful, remove the orderId from the session
            session.removeAttribute("orderId");
            return "redirect:/checkout/order/payment/success";
        } catch (Exception e) {
            log.error("Error processing payment", e);
            // if there is an error, do not throw an exception, but redirect to the product list
            session.removeAttribute("orderId");
            return "redirect:/products/lists";
        }
    }

    /**
     * payment success page
     * GET /checkout/order/payment/success
     */
    @GetMapping("/order/payment/success")
    public String paymentSuccess(HttpSession session, Model model) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        Order order = orderService.findPaidOrder(userId);
        model.addAttribute("currentPaidOrder", order);
        return "payment/payment-success";
    }


}
