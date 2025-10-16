package com.example.ShoppingCart.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.ShoppingCart.interfacemethods.OrderInterface;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.service.AlipayImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AlipayController {
    @Value("${custom.alipay.alipay-public-key}")
    private String alipayPublicKey;

    @Autowired
    private OrderInterface orderService;

    @Autowired
    private AlipayImplementation alipayService;
    /**
     * if it is Alipay, click Confirm after POSTing here, directly return the QR code page
     */
    @GetMapping("/Alipay")
    public String alipayForm(Model model, HttpSession session, @RequestParam(required = false) String paymentMethod) {
        try {
            String orderId= (String) session.getAttribute("orderId");
            Order order = orderService.findByOrderId(orderId);
            String form =alipayService.createFormPay(order); // the link returned by Alipay
            model.addAttribute("alipayForm", form);         // put into the model
            return "payment/alipaylogin"; // corresponding templates/payment/alipaylogin.html
        } catch (Exception e) {
            model.addAttribute("err", e.getMessage());
            return "payment/error"; // error page
        }
    }
    @PostMapping("/api/alipay/notify")
    @ResponseBody
    public String notify(HttpServletRequest req) throws AlipayApiException {
        Map<String, String> params = new HashMap<>();
        req.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        // 1. verify the signature
        boolean signOk = AlipaySignature.rsaCheckV1(
                params,                     // 1. parameter Map
                alipayPublicKey,     // 2. public key string
                "UTF-8",                            // 3. character set
                "RSA2");                            // 4. signature type
        if (!signOk) return "fail";
        String tradeStatus = req.getParameter("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
            String outTradeNo = req.getParameter("out_trade_no");
            Order order = orderService.findByOrderId(outTradeNo);
            orderService.createPaymentRecord("alipay", order);
        }
        return "success";
    }
    @GetMapping("/pay-success")
    public String paySuccess(HttpSession session) {
        session.removeAttribute("orderId");
        return "redirect:/checkout/order/payment/success";
    }
}
