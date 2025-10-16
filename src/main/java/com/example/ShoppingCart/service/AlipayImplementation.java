package com.example.ShoppingCart.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.repository.CartRepository;
import com.example.ShoppingCart.repository.PaymentRecordRepository;

import jakarta.transaction.Transactional;

@Service
public class AlipayImplementation {
    private final AlipayClient alipayClient;
    private final String notifyUrl;
    @Autowired
    private PaymentRecordRepository paymentrepo;
    @Autowired
    private ProductImplementation productImplementation;
    @Autowired
    private CartRepository cartrepo;


    public AlipayImplementation(
            @Value("${custom.alipay.app-private-key}") String privateKey,
            @Value("${custom.alipay.app-public-key}") String alipayPublicKey,
            @Value("${custom.alipay.app-id}") String appId,
            @Value("${custom.notify-url}") String notifyUrl  // 回调地址也从配置读取
    ) throws AlipayApiException {
        this.notifyUrl = notifyUrl+"/api/alipay/notify";
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
        this.alipayClient=alipayClient;
    }
    /**
     * pass in order information, return a form HTML that can be directly displayed
     */

    @Transactional
    public String createFormPay(Order order) throws AlipayApiException {
        //use the Alipay API
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(order.getOrderId());
        model.setTotalAmount(
                order.getTotalAmount()
                        .multiply(BigDecimal.valueOf(5.6))
                        .setScale(2, RoundingMode.HALF_UP)   // Keep two decimal places.
                        .toPlainString()
        );
        String subject = order.getOrderItems().stream()
                .map(OrderItem::getProductName)   // only take the product name
                .collect(Collectors.joining("-")); // use - to connect
        // truncate if too long
        if (subject.length() > 200) subject = subject.substring(0, 200) + "...";
        model.setSubject(subject);
        request.setBizModel(model);
        request.setReturnUrl("http://localhost:8080/pay-success");
        request.setNotifyUrl(notifyUrl);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        AlipayTradePagePayResponse response =alipayClient.pageExecute(request, "POST");
        if (!response.isSuccess()) {
            throw new RuntimeException("Alipay下单失败：" + response.getMsg());
        }
        // return form page
        return response.getBody();
    }
}
