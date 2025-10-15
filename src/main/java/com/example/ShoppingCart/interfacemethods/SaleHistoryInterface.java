
package com.example.ShoppingCart.interfacemethods;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import com.example.ShoppingCart.model.Product;


public interface SaleHistoryInterface {
    // get all orders of the user
    List<Order> getOrdersByUserId(String userId);

    // get the list of order items
    List<OrderItem> getOrderItemsByOrderId(String orderId);

    // get the product associated with the order item
    Product getProductByOrderItemId(String orderItemId);

    // get the payment record of the order
    PaymentRecord getPaymentRecordByOrderId(String orderId);

    Page<Order> getUserOrders(String userId, Pageable pageable);
}
