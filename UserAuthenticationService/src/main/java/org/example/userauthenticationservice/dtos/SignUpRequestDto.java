package org.example.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userauthenticationservice.models.Role;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private List<Role> roles;
}
