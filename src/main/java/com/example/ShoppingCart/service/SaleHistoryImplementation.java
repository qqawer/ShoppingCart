package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.SaleHistoryInterface;
import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.model.Product;
import com.example.ShoppingCart.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    //通过userid去找其所有的order
    public List<Order> getOrdersByUserId(String userId) {
        return orderrepo.findByUserId(userId);
    }

    @Override
    //通过orderid找order id其下对应的所有的orderitem
    public List<OrderItem> getOrderItemsByOrderId(String orderId) {
        return orderitemrepo.findOrderItemByOrderId(orderId);
    }

    @Override
    //通过order item id调用其所属的product id
    public Product getProductByOrderItemId(String orderItemId) {
        // 修正了变量名不一致的问题（itemrepo → orderItemRepo）
        Optional<OrderItem> orderItemOpt = orderitemrepo.findByOrderItemId(orderItemId);
        if (orderItemOpt.isPresent()) {
            return orderItemOpt.get().getProduct(); //  从OrderItem中获取关联的Product
        } else {
            throw new RuntimeException("OrderItem not found with id: " + orderItemId);
        }
    }

    @Override
    //通过order id 调用 payment record
    public PaymentRecord getPaymentRecordByOrderId(String orderId) {
        return paymentrepo.findByOrderId(orderId);
    }

    @Override
    public Page<Order> getUserOrders(String userId, Pageable pageable) {
        return orderrepo.findUserOrders(userId, pageable);
    }
}
