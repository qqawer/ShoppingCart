package com.example.ShoppingCart.exception;

import com.example.ShoppingCart.exception.errorcode.ErrorCode;

/**
 * business exception class: only used for business logic errors (e.g. "user not found")
 */
public class BusinessException extends RuntimeException {
    // error code (from ErrorCode enum)
    private final int code;

    // constructor 1: directly pass in ErrorCode enum (no dynamic parameters)
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // exception message = enum message
        this.code = errorCode.getCode();
    }

    // constructor 2: pass in ErrorCode enum + dynamic parameters (fill in placeholder)
    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(args)); // dynamically fill in message (e.g. "parameter error: phone number")
        this.code = errorCode.getCode();
    }

    // getter: for global exception handler to get code
    public int getCode() {
        return code;
    }
}