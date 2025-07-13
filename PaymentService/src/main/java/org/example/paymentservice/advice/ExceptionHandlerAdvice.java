package org.example.paymentservice.advice;

import org.example.paymentservice.dtos.ExceptionResponseDto;
import org.example.paymentservice.exceptions.UserNotAuthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({IllegalArgumentException.class, ArrayIndexOutOfBoundsException.class})
    public ResponseEntity<ExceptionResponseDto> handleExceptions(Exception ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserNotAuthenticatedException(UserNotAuthenticatedException ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
