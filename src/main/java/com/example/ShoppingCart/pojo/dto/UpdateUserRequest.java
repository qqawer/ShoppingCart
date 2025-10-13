package com.example.ShoppingCart.pojo.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {

    //@Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    @Pattern(regexp = "^$|\\S{2,50}$", message = "用户名长度必须在2-50个字符之间")
    private String userName;

    private String avatar;

    //@Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^$|\\S{6,20}$", message = "密码长度必须在6-20个字符之间")
    private String newPassword;

    private String oldPassword;

    // Address fields (optional) - minimal addition to allow saving a single address via profile edit
    @Pattern(regexp = "^$|^[\\p{L}\\s]{2,50}$", message = "收件人姓名应为2-50个字符，仅限字母或中文")
    private String receiverName;

    @Pattern(
            regexp = "^$|(\\+65\\s?|65\\s?)?[89]\\d{7}$",
            message = "请输入有效的新加坡手机号（如 +65 91234567 或 81234567）"
    )
    private String phone;

    @Pattern(regexp = "^$|^[A-Za-z\\s]{2,50}$", message = "地区名应为2-50个英文字符")
    private String region;

    @Pattern(
            regexp = "^$|^[A-Za-z0-9\\s,#-]{5,100}$",
            message = "详细地址应为5-100个字符，仅限字母、数字和常见符号"
    )
    private String detailAddress;

    // 是否设为默认地址
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
