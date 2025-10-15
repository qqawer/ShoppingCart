package com.example.ShoppingCart.model;

public class SessionConstant {
    /**
     * The key storing the user ID in the session
     */
    public static final String USER_ID = "userId";

    /**
     *  (key: storing the complete user information in the session)
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * private constructor, prevent instantiation
     */
    private SessionConstant() {
        throw new AssertionError("constant class cannot be instantiated");
    }
}
