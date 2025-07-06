package org.example.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userauthenticationservice.models.Role;

import java.util.List;

@Getter
@Setter
public class LogInResponseDto {
    private String username;
    private String email;
    private String message;
}
