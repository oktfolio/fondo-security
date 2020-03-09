package com.oktfolio.fondo.exception;

import com.oktfolio.fondo.common.result.ResultCodeEnum;
import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.common.result.UserResultCodeEnum;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
@RestControllerAdvice()
public class ExceptionHandlers {

    /**
     * 处理 UnauthorizedException 异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity unauthorizedExceptionHandler(UnauthorizedException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResultEntity.builder()
                        .code(ResultCodeEnum.UNAUTHORIZED.value())
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    /**
     * 处理 InvalidLoginStatusException 异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = InvalidLoginStatusException.class)
    public ResponseEntity invalidLoginStatusExceptionHandler(InvalidLoginStatusException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResultEntity.unauthorized(UserResultCodeEnum.INVALID_LOGIN_STATUS));
    }

    /**
     * 处理 InvalidTokenException 异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity invalidTokenExceptionHandler(InvalidTokenException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResultEntity.unauthorized(UserResultCodeEnum.INVALID_TOKEN));
    }


    /**
     * 处理 ExpiredJwtException 异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity expiredJwtExceptionHandler(ExpiredJwtException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResultEntity.unauthorized(UserResultCodeEnum.INVALID_TOKEN));
    }

    /**
     * 处理 Validator 异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity bindExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        HashMap map = new HashMap<String, String>(16);
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity
                .badRequest()
                .body(ResultEntity.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(ResultCodeEnum.INVALID_PARAMS.value())
                        .message(ResultCodeEnum.INVALID_PARAMS.message())
                        .data(map)
                        .build());
    }

    /**
     * 运行时异常，未处理的运行时异常都走这里
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity runtimeExceptionHandler(RuntimeException exception) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResultEntity
                        .builder()
                        .code(ResultCodeEnum.INTERNAL_SERVER_ERROR.value())
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
