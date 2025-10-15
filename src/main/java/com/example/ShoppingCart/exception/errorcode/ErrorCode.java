package com.example.ShoppingCart.exception.errorcode;

/**
 * error code enum class: manage all error codes and prompt information
 * coding rules:
 * - 4xxx: client error (such as parameter error, permission error)
 * - 5xxx: server error (such as database error, system error)
 * - by module: 40xx=general client error, 41xx=user module error, 42xx=order module error...
 */
public enum ErrorCode {
    // general client error (40xx)
    SUCCESS(200, "operation successful"),
    PARAM_ERROR(4001, "Parameter error：%s"), // with placeholder, dynamically fill in specific parameters
    NOT_LOGIN(4002, "please login first"),
    NO_PERMISSION(4003, "no permission to access"),

    // user module error (41xx)
    USER_NOT_FOUND(4101, "user not found"),
    USER_NAME_DUPLICATE(4102, "username already exists"),
    PASSWORD_ERROR(4103, "password error"),
    PHONE_NUMBER_DUPLICATE(4104, "phone number already registered"),
    PASSWORD_NOT_MATCH(4105, "two passwords entered are not consistent"),
    OLD_PASSWORD_ERROR(4106, "old password error"),

    // order module error (42xx)
    ORDER_NOT_FOUND(4201, "order not found"),
    ORDER_STATUS_ERROR(4202, "order status error, current status: %s"),

    //Product module error (43xx)
    PRODUCT_NOT_EXIST(4301, "The product does not exist,productId:%s"),
    PRODUCT_LIST_EMPTY(4302, "There are currently no products available"),
    PARAM_CANNOT_BE_NULL (4303, "Request parameter cannot be empty:%s"),
    PRODUCT_NAME_DUPLICATE(4304, "Product name already exists:%s"),
    PRODUCT_STOCK_NOT_ENOUGH(4305, "product stock not enough, current stock: %s"),

    // server error (5xxx)
    DB_ERROR(5001, "database operation failed"),
    SYSTEM_ERROR(5002, "system internal error, please try again later"),
    REDIS_ERROR(5003, "cache service exception");

    // error code
    private final int code;
    // error message (supports %s placeholder)
    private final String message;

    // constructor (enum class must be private)
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // getter method
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * dynamically fill in the placeholder in the message (e.g. PARAM_ERROR.getMessage("phone number format error"))
     */
    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}