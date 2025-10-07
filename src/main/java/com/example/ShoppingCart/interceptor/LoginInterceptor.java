package com.example.ShoppingCart.interceptor;


import com.example.ShoppingCart.model.SessionConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component

public class LoginInterceptor implements HandlerInterceptor {
        private static final Logger log= LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {
        // 使用统一的 Session 常量
        String userId = (String) request.getSession().getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            log.info("用户未登录，拦截请求: {}", request.getRequestURI());


                response.sendRedirect("/login");
            return false;
            }
        return true;
        }
    }

