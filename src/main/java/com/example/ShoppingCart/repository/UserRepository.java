package com.example.ShoppingCart.repository;

import com.example.ShoppingCart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    /**
     * 根据用户名查询用户（用于登录）
     */
    User findByUserName(String userName);

    /**
     * 根据手机号查询用户（用于登录）
     */
    User findByPhoneNumber(String phoneNumber);
}
