package com.example.ShoppingCart.exception;

import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理 @Controller 的异常（页面类Controller）
 */
@ControllerAdvice(annotations = Controller.class)
public class WebExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException e) {
        log.error("Business exception in web controller: code={}, message={}", e.getCode(), e.getMessage());
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorCode", e.getCode());
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ModelAndView handleIllegalStateException(IllegalStateException e) {
        log.error("IllegalStateException in web controller", e);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorCode", ErrorCode.SYSTEM_ERROR.getCode());
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleSystemException(Exception e) {
        log.error("System exception in web controller", e);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorCode", ErrorCode.SYSTEM_ERROR.getCode());
        mav.addObject("errorMessage", ErrorCode.SYSTEM_ERROR.getMessage());
        return mav;
    }
}
