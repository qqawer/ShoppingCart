package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OrderInterface {


    //checkout
    //2.createOrder
    public Order createOrder(@RequestBody Order order);

    //calculateTotalAmount
    public double calculateTotalPrice(@RequestBody Order order);

    //updateOrderStatus
    public void updateOrderStatus(@RequestBody Order order);

    //createOrderItems
    public List<OrderItem> createOrderItem(@RequestBody OrderItem orderItem);

    //createPayment
    public PaymentRecord createPaymentRecord(@RequestBody PaymentRecord paymentRecord);
}
