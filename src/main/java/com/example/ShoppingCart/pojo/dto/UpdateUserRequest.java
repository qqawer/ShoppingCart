package com.example.ShoppingCart.pojo.dto;

import jakarta.validation.constraints.Pattern;

public class UpdateUserRequest {

    //@Size(min = 2, max = 50, message = "User name length must be between 2-50 characters")
    @Pattern(regexp = "^$|\\S{2,50}$", message = "User name length must be between 2-50 characters")
    private String userName;

    private String avatar;

    //@Size(min = 6, max = 20, message = "Password length must be between 6-20 characters")
    @Pattern(regexp = "^$|\\S{6,20}$", message = "Password length must be between 6-20 characters")
    private String newPassword;

    private String oldPassword;

    // Address fields (optional) - minimal addition to allow saving a single address via profile edit
    @Pattern(regexp = "^$|^[\\p{L}\\s]{2,50}$", message = "Receiver name should be 2-50 characters, only letters or Chinese")
    private String receiverName;

    @Pattern(
            regexp = "^$|(\\+65\\s?|65\\s?)?[89]\\d{7}$",
            message = "Please enter a valid Singapore mobile phone number (e.g. +65 91234567 or 81234567)"
    )
    private String phone;

    @Pattern(regexp = "^$|^[A-Za-z\\s]{2,50}$", message = "Region name should be 2-50 English characters")
    private String region;

    @Pattern(
            regexp = "^$|^[A-Za-z0-9\\s,#-]{5,100}$",
            message = "Detailed address should be 5-100 characters, only letters, numbers and common symbols"
    )
    private String detailAddress;

    // whether to set as default address
    private Boolean isDefault;

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
