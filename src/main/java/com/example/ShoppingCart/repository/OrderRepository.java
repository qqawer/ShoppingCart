package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {

    Optional<Order> findByUser_UserIdAndOrderStatus(String userUserId, Integer orderStatus);

    Order findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(String userUserId, Integer orderStatus);

    // 通过userid 找到order
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId")
    List<Order> findByUserId(String userId);

}
