package org.example.userauthenticationservice.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.userauthenticationservice.models.Role;
import org.example.userauthenticationservice.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@Setter
public class SignUpResponseDto {
    private String name;
    private String email;
    private List<Role> roles;
    private String message;
}
