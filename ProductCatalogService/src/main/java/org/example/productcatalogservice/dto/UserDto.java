package org.example.productcatalogservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.productcatalogservice.model.Role;

import java.util.List;

@Data
public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;
}
