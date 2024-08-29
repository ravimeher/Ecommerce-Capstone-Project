package org.example.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userauthenticationservice.models.Role;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String email;
    private Set<Role> roleSet;
}
