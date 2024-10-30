package org.example.userauthenticationservice.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.models.User;
import org.springframework.util.MultiValueMap;

public interface IAuthService {
    User signUp(String email,String password);
    Pair<User, MultiValueMap<String, String>> logIn(String email, String password);
    Boolean validateToken(String token,Long userId);
}
