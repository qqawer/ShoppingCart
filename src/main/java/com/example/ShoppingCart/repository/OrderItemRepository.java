package com.example.ShoppingCart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ShoppingCart.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,String> {

    //find orderitem by orderid
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findOrderItemByOrderId(String orderId);
    //extract product id from orderitem
    @Query("SELECT oi FROM OrderItem oi WHERE oi.itemId = :itemId")
    Optional<OrderItem> findByOrderItemId(@Param("itemId") String itemId);
}
