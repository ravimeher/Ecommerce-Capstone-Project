package org.example.productcatalogservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDto {
    private Long userId;
    private String token;
}
