package org.example.paymentservice.dtos;

import lombok.Data;


import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Role> roles;
}
