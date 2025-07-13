package org.example.productcatalogservice.advice;

import org.example.productcatalogservice.dto.ExceptionResponseDto;
import org.example.productcatalogservice.exceptions.CategoryNotFoundException;
import org.example.productcatalogservice.exceptions.UserNotAuthenticatedException;
import org.example.productcatalogservice.exceptions.UserNotAuthorised;
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
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({UserNotAuthenticatedException.class, UserNotAuthorised.class})
    public ResponseEntity<ExceptionResponseDto> handleUserNotAuthenticatedException(CategoryNotFoundException ex) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(ex.getMessage());
        response.setExceptionType(ex.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
