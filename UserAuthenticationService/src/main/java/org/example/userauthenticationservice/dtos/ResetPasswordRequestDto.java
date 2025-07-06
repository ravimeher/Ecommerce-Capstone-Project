package org.example.userauthenticationservice.dtos;

import lombok.Data;

@Data
public class ResetPasswordRequestDto
{
    private String token;
    private String newPassword;
}
