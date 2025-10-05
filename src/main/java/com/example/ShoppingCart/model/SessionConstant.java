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
     * 私有构造函数，防止实例化
     */
    private SessionConstant() {
        throw new AssertionError("常量类不能实例化");
    }
}
