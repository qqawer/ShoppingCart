package com.example.ShoppingCart.service;

import com.example.ShoppingCart.exception.BusinessException;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.interfacemethods.UserInterface;
import com.example.ShoppingCart.model.SessionConstant;
import com.example.ShoppingCart.model.User;
import com.example.ShoppingCart.pojo.dto.LoginRequest;
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
    public UserInfoDTO login( LoginRequest request, HttpSession session, BindingResult bindingResult) {



        // 1. 先尝试用用户名查询
        User user = userRepository.findByUserName(request.getUsername());

        // 2. 如果用户名查不到，再用手机号查询
        if (user == null) {
            user = userRepository.findByPhoneNumber(request.getUsername());
        }

        // 3. 如果还是查不到，说明用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 4. 验证密码（简化版：直接比较明文）
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 5. 登录成功，将用户信息存入 Session
        session.setAttribute(SessionConstant.USER_ID, user.getUserId());
        UserInfoDTO userInfo = new UserInfoDTO(user);
        session.setAttribute(SessionConstant.CURRENT_USER, userInfo);

        // 6. 返回用户信息
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
}
