package com.example.ShoppingCart.interfacemethods;

import com.alipay.api.AlipayApiException;
import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;


import com.example.ShoppingCart.model.Product;


public interface OrderInterface {


    //checkout
    //2.createOrder and orderItem
    Order createOrder(String userId);

    //calculateTotalAmount
    BigDecimal calculateTotalPrice(List<CartRecord> cartRecords);

    //updateOrderStatus
    void updateOrderStatus(Order order,int orderStatus);

    //createPayment
    PaymentRecord createPaymentRecord(String paymentMethod, Order order);

    Order findPendingOrder(String userId);

    Order findPaidOrder(String userId);


    void cancelOrder(Order order);

    Order findByOrderId(String orderId);
    
    // 更新订单信息
    void updateOrder(Order order);

}
