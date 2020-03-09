package com.oktfolio.fondo.exception;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/18
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
