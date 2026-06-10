package com.aieppay.task.exception;


public class ServiceException extends RuntimeException {

    private Integer code;

    public ServiceException(String message) {
        super(message);
        this.code = 400;
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public static ServiceException of(String message) {
        return new ServiceException(message);
    }

    public static ServiceException of(Integer code, String message) {
        return new ServiceException(code, message);
    }

    public Integer getCode() { return code; }
}
