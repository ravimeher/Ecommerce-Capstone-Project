package org.example.userauthenticationservice.services;

import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User getUser(Long uid) {
        Optional<User> user = userRepo.findById(uid);
        System.out.println(user.get().getEmail());
        return user.orElse(null);
    }
}
