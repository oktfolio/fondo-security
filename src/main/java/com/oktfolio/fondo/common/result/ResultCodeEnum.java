package com.oktfolio.fondo.common.result;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/14
 */
public enum ResultCodeEnum implements ResultCode{

    // Success
    SUCCESS(20000, "success"),
    // Error
    ERROR(40000, "error"),
    // invalid params
    INVALID_PARAMS(40010, "invalid params"),
    // Internal server error
    INTERNAL_SERVER_ERROR(50000, "internal server error"),
    // unauthorized
    UNAUTHORIZED(40100, "unauthorized"),
    // forbidden
    FORBIDDEN(40300, "forbidden"),
    // need to login
    NEED_LOGIN(40301, "need to login"),
    // no permission to access
    NO_PERMISSION(40300, "no permission to access"),
    // not found
    NOT_FOUND(40400, "not found");


    private final int value;
    private final String message;

    ResultCodeEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    @Override
    public int value() {
        return this.value;
    }

    @Override
    public String message() {
        return this.message;
    }
}
