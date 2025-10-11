
package com.example.ShoppingCart.interfacemethods;

import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;

import java.util.List;


import com.example.ShoppingCart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface SaleHistoryInterface {
    // 获取用户所有订单
    List<Order> getOrdersByUserId(String userId);

    // 获取订单项列表
    List<OrderItem> getOrderItemsByOrderId(String orderId);

    // 获取订单项关联的产品
    Product getProductByOrderItemId(String orderItemId);

    // 获取订单支付记录
    PaymentRecord getPaymentRecordByOrderId(String orderId);

    Page<Order> getUserOrders(String userId, Pageable pageable);
}
