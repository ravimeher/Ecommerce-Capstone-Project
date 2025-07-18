package com.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponseDto {
    private String message;
    private String stackTrace;
    private String exceptionType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
}
