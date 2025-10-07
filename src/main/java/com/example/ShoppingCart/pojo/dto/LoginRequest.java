package com.example.ShoppingCart.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    /**
     * 用户名或手机号
     */
    @NotBlank(message = "Please enter Phone Number")
    @Size(min = 3, max = 12, message = "Code must be between 3-12 characters")
    private String username;

    /**
     * 密码（明文）
     */
    @NotBlank(message = "Please enter correct password")
    @Size(min = 8, max = 30, message = "Code must be between 8-20 characters")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}
