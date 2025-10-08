package com.example.ShoppingCart.service;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
import com.example.ShoppingCart.pojo.dto.RegisterRequest;
import com.example.ShoppingCart.pojo.dto.UpdateUserRequest;
import com.example.ShoppingCart.pojo.dto.UserInfoDTO;
import com.example.ShoppingCart.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@Transactional
public class UserImplementation implements UserInterface {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserInfoDTO login(LoginRequest request, HttpSession session, BindingResult bindingResult) {

        // 1. 通过手机号查询用户
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber());

        // 2. 如果查不到，说明用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. 验证密码（明文比较）
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 4. 登录成功，将用户信息存入 Session
        session.setAttribute(SessionConstant.USER_ID, user.getUserId());
        UserInfoDTO userInfo = new UserInfoDTO(user);
        session.setAttribute(SessionConstant.CURRENT_USER, userInfo);

        // 5. 返回用户信息
        return userInfo;
    }

    @Override
    public void logout(HttpSession session) {
        // 销毁整个 Session
        session.invalidate();
    }

    @Override
    public UserInfoDTO getCurrentUser(HttpSession session) {
        // 从 Session 获取用户信息
        UserInfoDTO userInfo = (UserInfoDTO) session.getAttribute(SessionConstant.CURRENT_USER);

        if (userInfo == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        return userInfo;
    }

    @Override
    public boolean isLoggedIn(HttpSession session) {
        String userId = (String) session.getAttribute(SessionConstant.USER_ID);
        return userId != null && !userId.isEmpty();
    }

    @Override
    public UserInfoDTO register(RegisterRequest request) {
        // 1. 验证两次密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        // 2. 检查手机号是否已被注册
        User existingUser = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.PHONE_NUMBER_DUPLICATE);
        }

        // 3. 检查用户名是否已被占用
        User existingUserName = userRepository.findByUserName(request.getUserName());
        if (existingUserName != null) {
            throw new BusinessException(ErrorCode.USER_NAME_DUPLICATE);
        }

        // 4. 创建新用户
        User newUser = new User();
        newUser.setUserName(request.getUserName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        // 5. 设置密码（明文存储）
        newUser.setPassword(request.getPassword());

        // 6. 保存到数据库
        User savedUser = userRepository.save(newUser);

        // 7. 返回用户信息
        return new UserInfoDTO(savedUser);
    }

    @Override
    public UserInfoDTO updateUserInfo(String userId, UpdateUserRequest request) {
        // 1. 查询用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 更新用户名
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            // 检查用户名是否已被其他用户占用
            User existingUser = userRepository.findByUserName(request.getUserName());
            if (existingUser != null && !existingUser.getUserId().equals(userId)) {
                throw new BusinessException(ErrorCode.USER_NAME_DUPLICATE);
            }
            user.setUserName(request.getUserName());
        }

        // 3. 更新头像
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            user.setAvatar(request.getAvatar());
        }

        // 4. 更新密码（如果提供了旧密码和新密码）
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            // 验证旧密码
            if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "请输入原密码");
            }
            if (!request.getOldPassword().equals(user.getPassword())) {
                throw new BusinessException(ErrorCode.OLD_PASSWORD_ERROR);
            }
            // 设置新密码（明文存储）
            user.setPassword(request.getNewPassword());
        }

        // 5. 保存更新
        User updatedUser = userRepository.save(user);

        // 6. 返回更新后的用户信息
        return new UserInfoDTO(updatedUser);
    }
}
