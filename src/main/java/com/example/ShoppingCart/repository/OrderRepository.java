package com.example.ShoppingCart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ShoppingCart.model.Order;

public interface OrderRepository extends JpaRepository<Order,String> {

    Optional<Order> findByUser_UserIdAndOrderStatus(String userUserId, Integer orderStatus);

    Order findFirstByUser_UserIdAndOrderStatusOrderByOrderTimeDesc(String userUserId, Integer orderStatus);

    //find order by userid
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId")
    List<Order> findByUserId(String userId);

    @Query("SELECT p FROM Order p WHERE p.user.userId = :userId ORDER BY p.orderTime DESC")
    Page<Order> findUserOrders(@Param("userId") String userId, Pageable pageable);

}
