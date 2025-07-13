package org.example.productcatalogservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponseDto {
    private String message;
    private String stackTrace;
    private String exceptionType;
}
