package com.oktfolio.fondo.exception;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
