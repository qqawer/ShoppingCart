package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.OrderInterface;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderImplementation implements OrderInterface {
    @Autowired
    private OrderRepository prepo;

    @Override
    public Order createOrder(Order order) {
        return null;
    }

    @Override
    public double calculateTotalPrice(Order order) {
        return 0;
    }

    @Override
    public void updateOrderStatus(Order order) {

    }

    @Override
    public List<OrderItem> createOrderItem(OrderItem orderItem) {
        return List.of();
    }

    @Override
    public PaymentRecord createPaymentRecord(PaymentRecord paymentRecord) {
        return null;
    }
}
