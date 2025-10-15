package com.example.ShoppingCart.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "User name cannot be empty")
    @Size(min = 2, max = 50, message = "User name length must be between 2-50 characters")
    private String userName;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[89]\\d{7}$", message = "Phone number format is incorrect (Singapore phone number)")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password length must be between 6-20 characters")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty")
    private String confirmPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
