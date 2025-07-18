package org.example.userauthenticationservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userauthenticationservice.client.KafkaProducerClient;
import org.example.userauthenticationservice.dtos.ResetPasswordRequestDto;
import org.example.userauthenticationservice.dtos.SendEmailDto;
import org.example.userauthenticationservice.exceptions.*;
import org.example.userauthenticationservice.models.PasswordResetToken;
import org.example.userauthenticationservice.models.Role;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repositories.ResetTokenRepo;
import org.example.userauthenticationservice.repositories.RoleRepo;
import org.example.userauthenticationservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private ResetTokenRepo resetTokenRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private KafkaProducerClient kafkaProducerClient;
    @Autowired
    private ObjectMapper objectMapper;

    public User getUser(Long uid) {
        Optional<User> user = userRepo.findById(uid);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User not Found");
        }
        return user.get();
    }

    public User signUp(String name,String email, String password, List<Role> roles) throws JsonProcessingException {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent()){
            throw new UserAlreadyExistsException("User already exists");
        }
        List<Role> finalRoles = validateRoles(roles);
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        newUser.setRoles(finalRoles);
        userRepo.save(newUser);
        //send email to user
        SendEmailDto sendEmailDto=new SendEmailDto();
        sendEmailDto.setTo(newUser.getEmail());
        sendEmailDto.setSubject("User Registration");
        sendEmailDto.setBody("Congratulations on Signing up");

        kafkaProducerClient.SendMessage("sendEmail",objectMapper.writeValueAsString(sendEmailDto));
        return newUser;
    }


    public String forgotPassword(String email) throws JsonProcessingException {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) throw new UserNotFoundException("User not Found");

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiry);
        resetTokenRepo.save(resetToken);

        String resetUrl = "http://localhost:9000/user/reset-password?token=" + token;

        SendEmailDto sendEmailDto=new SendEmailDto();
        sendEmailDto.setTo(user.getEmail());
        sendEmailDto.setSubject("Password Reset Link");
        sendEmailDto.setBody("Click here to reset password: " + resetUrl);

        // Send email using Kafka/EmailService
        kafkaProducerClient.SendMessage("sendEmail",objectMapper.writeValueAsString(sendEmailDto));

        return "Reset password link sent";
    }
    public String resetPassword(String token,String newPassword)
    {
        PasswordResetToken resetToken = resetTokenRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(user);

        resetTokenRepo.delete(resetToken); // Invalidate token

        return "Password reset successful";
    }

    public User editUserInfo( String name, String email, List<Role> roles) {
        User user = userRepo.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not Found"));
        user.setName(name);
        user.setEmail(email);

        validateRoles(roles);
        List<Role> finalRoles = validateRoles(roles);
        user.setRoles(finalRoles);
        return userRepo.save(user);
    }

    private List<Role> validateRoles(List<Role> roles) {
        Set<String> allowedRoles = Set.of("ADMIN", "USER");
        List<Role> finalRoles = new ArrayList<>();

        for(Role role : roles){
            String roleName = role.getValue();
            if(!allowedRoles.contains(roleName)){
                throw new InvalidRoleException("Role does not exist");
            }
            Role dbRole = (Role) roleRepo.findByValue(roleName)
                    .orElseGet(() -> roleRepo.save(new Role(roleName)));
            finalRoles.add(dbRole);
        }
        return finalRoles;
    }

}
