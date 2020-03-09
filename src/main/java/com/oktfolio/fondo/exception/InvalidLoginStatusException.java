package com.oktfolio.fondo.exception;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/18
 */
public class InvalidLoginStatusException extends RuntimeException {

    public InvalidLoginStatusException() {
    }

    public InvalidLoginStatusException(String message) {
        super(message);
    }
}
