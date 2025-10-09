package com.example.ShoppingCart.service;

import com.example.ShoppingCart.interfacemethods.OrderInterface;
import com.example.ShoppingCart.model.*;
import com.example.ShoppingCart.repository.*;

import jakarta.transaction.Transactional;
import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private OrderItemRepository orderitemrepo;
    @Autowired
    private UserAddressRepository useraddressrepo;

    @Override
    public Order createOrder(String userId) {
        //先判断是否已存在订单，且不能为空购物车，如果订单存在使用原来订单，如果不存在建立新订单并且删除购物车里的物品
        Optional<Order> pendingorder = orderrepo.findByUser_UserIdAndOrderStatus(userId,0);
        if(pendingorder.isPresent()){
            return pendingorder.get();
        }
        List<CartRecord> cartRecords = cartrepo.findByUser_UserId(userId);
        //验证是否为空
        if(cartRecords.isEmpty()){
            throw new IllegalStateException("Shopping cart is empty");
        }
        //通过验证开始创建订单

        Order order = new Order();
        order.setUser(userrepo.findById(userId).orElseThrow());
        // 允许创建没有地址的订单，地址可以在确认页面添加
        UserAddress addr = useraddressrepo.findByUser_UserId(userId);
        order.setAddress(addr);  // 可以为 null
        order.setTotalAmount(calculateTotalPrice(cartRecords));
        order.setOrderStatus(0);                                //pending in default
        order.setOrderTime(LocalDateTime.now());
        order.setPaymentRecord(null);

        List<OrderItem> orderItems = new ArrayList<>();

        order=orderrepo.save(order);        //save order firstly

        for(CartRecord record:cartRecords){
            OrderItem orderItem = new OrderItem();

            orderItem.setBuyQuantity(record.getQuantity());
            orderItem.setProduct(record.getProduct());
            orderItem.setProductPrice(record.getProduct().getPrice());
            orderItem.setProductName(record.getProduct().getProductName());

            orderItem.setOrder(order);
            orderItems.add(orderItem);

        }
        order.setOrderItems(orderItems);
        //save orderItems
        orderitemrepo.saveAll(orderItems);
        //清空购物车防止反复下单
        cartrepo.deleteAll(cartRecords);

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
    public void updateOrderStatus(Order order,int newOrderStatus) {

        order.setOrderStatus(newOrderStatus);
        orderrepo.save(order);
    }



    @Override
    public PaymentRecord createPaymentRecord(String paymentMethod,Order order) {
        // validate payment method and create record
        if (paymentMethod == null || paymentMethod.isEmpty()){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "paymentMethod");
        }
        // whitelist supported payment methods
        String pm = paymentMethod.toLowerCase();
        if (!(pm.equals("alipay") || pm.equals("paynow") || pm.equals("visa"))){
            throw new BusinessException(ErrorCode.PARAM_ERROR, "paymentMethod");
        }

        PaymentRecord newRecord = new PaymentRecord();
        newRecord.setPaymentMethod(pm);
        newRecord.setPaymentAmount(order.getTotalAmount());
        newRecord.setPaymentTime(LocalDateTime.now());
        newRecord.setTradeNo(UUID.randomUUID().toString());             //不应该是前端生成
        newRecord.setPayStatus(1);                                     //paymentrecord 有status？
        newRecord.setOrder(order);
        order.setOrderStatus(1);                                        //"paid" status
        order.setPaymentRecord(newRecord);
        newRecord=paymentrepo.save(newRecord);
        orderrepo.save(order);
        return  newRecord;
    }

    @Override
    public Order findPendingOrder(String userId){
        Order order= orderrepo.findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(userId,0);
        if(order==null){
            throw new IllegalStateException("PendingOrder is empty");
        }
        return order;
    }

    @Override
    public Order findPaidOrder(String userId){
        Order order= orderrepo.findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(userId,1);
        if(order==null){
            throw new IllegalStateException("PaidOrder is empty");
        }
        return order;
    }

    @Override
    public void cancelOrder(Order order){
        orderrepo.delete(order);
    }

}
