package com.example.ShoppingCart.interceptor;


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
         String userId =(String) request.getSession().getAttribute("userId");
         if (userId==null){
             log.info("not login");
             response.sendRedirect("/login");
             return  false;
         }
         return  true;
     }
}
