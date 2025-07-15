package com.example.cartservice.advice;


import com.example.cartservice.dtos.ExceptionResponseDto;
import com.example.cartservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({UserCartNotFoundException.class, CartItemNotFoundException.class,InvalidProductIdProvidedException.class})
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(RuntimeException ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ProductOutOfStockException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionResponseDto> handleBadRequestException(RuntimeException ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ExceptionResponseDto> handleAuthFailure(UserNotAuthenticatedException ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleGeneral(Exception ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

