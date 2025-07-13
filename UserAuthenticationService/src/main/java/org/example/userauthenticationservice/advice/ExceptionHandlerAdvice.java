package org.example.userauthenticationservice.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.userauthenticationservice.dtos.ExceptionResponseDto;
import org.example.userauthenticationservice.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({UserAlreadyExistsException.class,InvalidTokenException.class,SessionAlreadyExistsException.class})
    public ResponseEntity<ExceptionResponseDto> handleUserAlreadyExistsException(Exception e) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(e.getMessage());
        response.setExceptionType(e.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler({InvalidPasswordException.class})
    public ResponseEntity<ExceptionResponseDto> handleInvalidPasswordException(InvalidPasswordException e) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(e.getMessage());
        response.setExceptionType(e.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler({InvalidRoleException.class, UserNotFoundException.class})
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(Exception e) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(e.getMessage());
        response.setExceptionType(e.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ExceptionResponseDto> handleJsonError(JsonProcessingException e) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage("Error while serializing JSON");
        response.setExceptionType(e.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleRunTimeException(RuntimeException e) {
        ExceptionResponseDto response = new ExceptionResponseDto();
        response.setMessage(e.getMessage());
        response.setExceptionType(e.getClass().getSimpleName());
        response.setStackTrace(Arrays.toString(e.getStackTrace()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
