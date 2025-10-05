package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem,String> {

    //通过orderid 找到orderitem
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    List<OrderItem> findOrderItemByOrderId(String orderId);
    //orderitem 里将product id提取出来
    @Query("SELECT oi FROM OrderItem oi WHERE oi.itemId = :itemId")
    Optional<OrderItem> findByOrderItemId(@Param("itemId") String itemId);
}
