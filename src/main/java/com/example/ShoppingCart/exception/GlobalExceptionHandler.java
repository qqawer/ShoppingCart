package com.example.ShoppingCart.exception;

import com.example.ShoppingCart.pojo.dto.ResponseMessage;
import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)   // 保证优先
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseMessage handleBusiness(BusinessException e) {
        return new ResponseMessage(e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseMessage handleAll(Exception e) {
        log.error("系统异常", e);
        return new ResponseMessage(ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getMessage(), null);
    }
}
