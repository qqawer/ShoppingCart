package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.CartRecord;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

public interface OrderInterface {


    //checkout
    //2.createOrder and orderItem
    Order createOrder(String userId);

    //calculateTotalAmount
    BigDecimal calculateTotalPrice(List<CartRecord> cartRecords);

    //updateOrderStatus
    void updateOrderStatus(String orderId,int orderStatus);

    //createPayment
    PaymentRecord createPaymentRecord(String paymentMethod, Order order);
}
