package com.example.ShoppingCart.interceptor;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.ShoppingCart.model.SessionConstant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component

public class LoginInterceptor implements HandlerInterceptor {
        private static final Logger log= LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {
        // use the unified Session constant
        String userId = (String) request.getSession().getAttribute(SessionConstant.USER_ID);

        if (userId == null) {
            log.info("user not logged in, intercept request: {}", request.getRequestURI());


                response.sendRedirect("/login");
            return false;
            }
        return true;
        }
    }

