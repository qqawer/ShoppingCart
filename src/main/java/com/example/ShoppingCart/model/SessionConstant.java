package com.example.ShoppingCart.model;

public class SessionConstant {
    /**
     * Session 中存储用户 ID 的 key
     */
    public static final String USER_ID = "userId";

    /**
     * Session 中存储完整用户信息的 key
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * private constructor, prevent instantiation
     */
    private SessionConstant() {
        throw new AssertionError("常量类不能实例化");
    }
}
