package org.example.userauthenticationservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.dtos.*;
import org.example.userauthenticationservice.exceptions.InvalidTokenException;
import org.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    //Signup
    //Login
    //ForgetPassword
    //Logout
    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        //Validation - check if user is not present already
        try{
            User user = authService.signUp(signUpRequestDto.getEmail(),signUpRequestDto.getPassword());
            if(user == null){
                throw new UserAlreadyExistsException("Email already Exists. Enter another Email");
            }

            return new ResponseEntity<>(from(user),HttpStatus.CREATED);
        }
        catch (UserAlreadyExistsException | JsonProcessingException ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> logIn(@RequestBody LogInRequestDto logInRequestDto){
        Pair<User, MultiValueMap<String,String>> userWithHeaders = authService.logIn(logInRequestDto.getEmail(), logInRequestDto.getPassword());
        User user = userWithHeaders.a;
        if(user == null) {
            throw new RuntimeException("BAD CREDENTIALS");
        }

        return new ResponseEntity<>(from(user),userWithHeaders.b,HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody ValidateTokenDto validateTokenDto){
        try {
            System.out.println(validateTokenDto.getToken());
            Boolean response = authService.validateToken(validateTokenDto.getToken(), validateTokenDto.getUserId());
            if (response == false) {
                throw new InvalidTokenException("Either Token is stale or invalid");
            }
            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch(InvalidTokenException exception) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Boolean> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto){
        return null;
    }

    public ResponseEntity<String> logOut(@RequestBody LogOutRequestDto logOutRequestDto){
        return null;
    }

    private UserDto from (User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoleSet(user.getRoleSet());
        return userDto;
    }
}
