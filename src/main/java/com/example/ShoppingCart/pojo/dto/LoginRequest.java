package com.example.ShoppingCart.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    /**
     * phone number
     */
    @NotBlank(message = "Please enter an 8-digit Singapore mobile phone number.")
    @Pattern(regexp = "^[89]\\d{7}$", message = "Phone number format is incorrect")
    private String phoneNumber;

    /**
     * password (plain text)
     */
    @NotBlank(message = "Please enter a password")
    @Size(min = 6, max = 20, message = "Password length must be between 6-20 characters")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
                "phoneNumber='" + phoneNumber + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}
