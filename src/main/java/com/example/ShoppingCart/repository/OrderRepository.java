package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.Order;
import com.example.ShoppingCart.model.OrderItem;
import com.example.ShoppingCart.model.PaymentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {

    Optional<Order> findByUser_UserIdAndOrderStatus(String userUserId, Integer orderStatus);

    Order findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(String userUserId, Integer orderStatus);

    // 通过userid 找到order
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId")
    List<Order> findByUserId(String userId);

    @Query("SELECT p FROM Order p WHERE p.user.userId = :userId ORDER BY p.orderTime DESC")
    Page<Order> findUserOrders(@Param("userId") String userId, Pageable pageable);

}
