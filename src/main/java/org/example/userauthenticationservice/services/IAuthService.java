package org.example.userauthenticationservice.services;

import org.example.userauthenticationservice.models.User;

public interface IAuthService {
    User signUp(String email,String password);
    User logIn(String email,String password);
}
