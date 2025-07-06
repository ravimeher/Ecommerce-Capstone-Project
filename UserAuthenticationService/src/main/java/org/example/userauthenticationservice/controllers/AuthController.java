package org.example.userauthenticationservice.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.dtos.*;
import org.example.userauthenticationservice.exceptions.InvalidTokenException;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        SignUpResponseDto responseDto = new SignUpResponseDto();

        User user = authService.signUp(signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword(),
                signUpRequestDto.getRoles());

        responseDto.setEmail(user.getEmail());
        responseDto.setName(user.getName());
        responseDto.setRoles(user.getRoles());
        responseDto.setMessage("User Created Successfully");

        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> logIn(@RequestBody LogInRequestDto logInRequestDto){
        LogInResponseDto responseDto = new LogInResponseDto();

        Pair<User, MultiValueMap<String,String>> userWithHeaders = authService.logIn(logInRequestDto.getEmail(), logInRequestDto.getPassword());
        User user = userWithHeaders.a;
        responseDto.setUsername(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setMessage("User Logged In Successfully");
        return new ResponseEntity<>(responseDto,userWithHeaders.b,HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponseDto> validate(@RequestBody ValidateTokenRequestDto validateTokenDto){
        ValidateTokenResponseDto responseDto = new ValidateTokenResponseDto();
        System.out.println(validateTokenDto.getToken());
        Boolean response = authService.validateToken(validateTokenDto.getToken(), validateTokenDto.getUserId());
        if(response){
            responseDto.setMessage("Token Validated Successfully");
        }else {
            throw new InvalidTokenException("Invalid Token");
        }
        return new ResponseEntity<>(responseDto,HttpStatus.OK);

    }

    public ResponseEntity<Boolean> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto){
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<LogOutResponseDto> logOut(@RequestBody LogOutRequestDto logOutRequestDto){
        LogOutResponseDto responseDto = new LogOutResponseDto();

        String message = authService.logout(logOutRequestDto.getEmail());
        responseDto.setMessage(message);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
