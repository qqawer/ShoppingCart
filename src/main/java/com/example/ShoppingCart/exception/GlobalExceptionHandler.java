package com.example.ShoppingCart.exception;

import com.alipay.api.AlipayApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ShoppingCart.exception.errorcode.ErrorCode;
import com.example.ShoppingCart.pojo.dto.ResponseMessage;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)   // ensure priority
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //1.Bean Validation validation failed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage handleValidation(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        String msg = fe == null ? "Parameter error" : fe.getDefaultMessage();
        return new ResponseMessage(4001, msg, null);
    }

    //2. JSON format error or request body is empty
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseMessage handleJson(HttpMessageNotReadableException e) {
        return new ResponseMessage(4002, "Request body format error or required field missing", null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseMessage handleBusiness(BusinessException e) {
        return new ResponseMessage(e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseMessage handleAll(Exception e) {
        log.error("system exception", e);
        return new ResponseMessage(ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getMessage(), null);
    }

    /** 1. 支付宝 notify 专用：保证返回纯文本，避免 JSON/HTML */
    @ExceptionHandler(AlipayApiException.class)
    public String handleAlipay(AlipayApiException e) {
        log.warn("Alipay notify error: {}", e.getMessage());
        return "fail";          // 支付宝收到 fail 会重试
    }
}
