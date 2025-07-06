package org.example.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userauthenticationservice.models.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private String email;
    private List<Role> roles;
}
