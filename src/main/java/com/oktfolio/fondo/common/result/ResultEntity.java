package com.oktfolio.fondo.common.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultEntity {
    private Integer code;
    private String message;
    private Object data;
    @JsonIgnore
    private HttpStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;

    public Integer getCode() {
        return code;
    }

    private void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    private void setData(Object data) {
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    private void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    private ResultEntity() {
    }

    public ResultEntity code(Integer code) {
        this.setCode(code);
        return this;
    }

    public ResultEntity message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultEntity data(Object data) {
        this.setData(data);
        return this;
    }

    public ResultEntity status(HttpStatus status) {
        this.setStatus(status);
        return this;
    }

    public ResultEntity timestamp(LocalDateTime timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        ResultEntity resultEntity = new ResultEntity();

        public Builder code(Integer code) {
            resultEntity.code = code;
            return this;
        }

        public Builder message(String message) {
            resultEntity.message = message;
            return this;
        }

        public Builder data(Object data) {
            resultEntity.data = data;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            resultEntity.timestamp = timestamp;
            return this;
        }

        public Builder status(HttpStatus status) {
            resultEntity.status = status;
            return this;
        }

        public ResultEntity build() {
            return resultEntity;
        }
    }

    public static ResultEntity ok() {
        return ResultEntity.builder()
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResultEntity ok(Object data) {
        return ResultEntity.builder()
                .status(HttpStatus.OK)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResultEntity notFound(ResultCode resultCode) {
        return ResultEntity.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(resultCode.value())
                .message(resultCode.message())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResultEntity badRequest(ResultCode resultCode) {
        return ResultEntity.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(resultCode.value())
                .message(resultCode.message())
                .build();
    }

    public static ResultEntity unauthorized(ResultCode resultCode) {
        return ResultEntity.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .code(resultCode.value())
                .message(resultCode.message())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResultEntity forbidden(ResultCode resultCode) {
        return ResultEntity.builder()
                .status(HttpStatus.FORBIDDEN)
                .code(resultCode.value())
                .message(resultCode.message())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResultEntity created() {
        return ResultEntity.builder()
                .status(HttpStatus.CREATED)
                .build();
    }

    public static ResultEntity created(Object data) {
        return ResultEntity.builder()
                .data(data)
                .status(HttpStatus.CREATED)
                .build();
    }

    public static ResultEntity created(ResultCode resultCode) {
        return ResultEntity.builder()
                .status(HttpStatus.CREATED)
                .code(resultCode.value())
                .message(resultCode.message())
                .build();
    }

    public static ResultEntity internalServerError() {
        return ResultEntity.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ResultEntity internalServerError(ResultCode resultCode) {
        return ResultEntity.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(resultCode.value())
                .message(resultCode.message())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
