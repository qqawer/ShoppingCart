package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.OrderInterface;
import com.example.ShoppingCart.model.*;
import com.example.ShoppingCart.repository.*;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderImplementation implements OrderInterface {
    @Autowired
    private OrderRepository orderrepo;
    @Autowired
    private PaymentRecordRepository  paymentrepo;
    @Autowired
    private CartRepository cartrepo;
    @Autowired
    private UserRepository userrepo;
    @Autowired
    private ProductRepository productrepo;
    @Autowired
    private OrderItemRepository orderitemrepo;


    @Override
    public Order createOrder(String userId) {

        List<CartRecord> cartRecords = cartrepo.findByUser_UserId(userId);
        Order order = new Order();
        order.setUser(userrepo.findById(userId).orElseThrow());
        order.setTotalAmount(calculateTotalPrice(cartRecords));
        order.setAddress(orderrepo.findById(userId).orElseThrow().getAddress());
        order.setOrderStatus(0);                                //pending in default
        //order.setOrderItems(createOrderItem(cartRecords,order));
        order=orderrepo.save(order);        //save order firstly

        List<OrderItem> orderItems = new ArrayList<>();
        for(CartRecord record:cartRecords){
            OrderItem orderItem = new OrderItem();
            orderItem.setBuyQuantity(record.getQuantity());
            orderItem.setProduct(record.getProduct());
            orderItem.setProductPrice(record.getProduct().getPrice());
            orderItem.setProductName(record.getProduct().getProductName());

            orderItem.setOrder(orderrepo.findById(userId).orElseThrow());
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        //save orderItems
        orderitemrepo.saveAll(orderItems);
        return order;
    }

    @Override
    public BigDecimal calculateTotalPrice(List<CartRecord> cartRecord) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartRecord record :cartRecord) {
          BigDecimal itemTotal = record.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(record.getQuantity()));
          total = total.add(itemTotal);
        }

        return total;
    }

    @Override
    public void updateOrderStatus(String orderId,int orderStatus) {
        Order order = orderrepo.findById(orderId).orElseThrow();
        order.setOrderStatus(orderStatus);
        orderrepo.save(order);
    }



    @Override
    public PaymentRecord createPaymentRecord(String paymentMethod,Order order) {
    //how to identify?
        PaymentRecord newRecord = new PaymentRecord();
        newRecord.setPaymentMethod(paymentMethod);
        newRecord.setPaymentAmount(order.getTotalAmount());
        newRecord.setPaymentTime(LocalDateTime.now());
        newRecord.setTradeNo(UUID.randomUUID().toString());             //不应该是前端生成
        //newRecord.setPayStatus();                                     //paymentrecord 有status？
        order.setOrderStatus(1);                                        //"paid" status
        newRecord.setOrder(order);

        newRecord=paymentrepo.save(newRecord);
        return  newRecord;
    }
}
