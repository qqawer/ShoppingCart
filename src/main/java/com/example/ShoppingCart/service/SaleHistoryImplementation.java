package com.example.ShoppingCart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ShoppingCart.interfacemethods.SaleHistoryInterface;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.repository.OrderItemRepository;
import com.example.ShoppingCart.repository.OrderRepository;
import com.example.ShoppingCart.repository.PaymentRecordRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SaleHistoryImplementation implements SaleHistoryInterface {

    @Autowired
    private OrderRepository orderrepo;
    @Autowired
    private PaymentRecordRepository paymentrepo;
    @Autowired
    private OrderItemRepository orderitemrepo;

    @Override
    //find all orders by userid
    public List<Order> getOrdersByUserId(String userId) {
        return orderrepo.findByUserId(userId);
    }

    @Override
    //find all orderitems by orderid
    public List<OrderItem> getOrderItemsByOrderId(String orderId) {
        return orderitemrepo.findOrderItemByOrderId(orderId);
    }

    @Override
    //find product by order item id
    public Product getProductByOrderItemId(String orderItemId) {
        // corrected the variable name inconsistency (itemrepo → orderItemRepo)
        Optional<OrderItem> orderItemOpt = orderitemrepo.findByOrderItemId(orderItemId);
        if (orderItemOpt.isPresent()) {
            return orderItemOpt.get().getProduct(); // get the associated Product from OrderItem
        } else {
            throw new RuntimeException("OrderItem not found with id: " + orderItemId);
        }
    }

    @Override
    //find payment record by order id
    public PaymentRecord getPaymentRecordByOrderId(String orderId) {
        return paymentrepo.findByOrderId(orderId);
    }

    @Override
    public Page<Order> getUserOrders(String userId, Pageable pageable) {
        return orderrepo.findUserOrders(userId, pageable);
    }
}
