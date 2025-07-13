package org.example.userauthenticationservice.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.dtos.*;
import org.example.userauthenticationservice.exceptions.InvalidTokenException;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> logIn(@RequestBody LogInRequestDto logInRequestDto) {
        LogInResponseDto responseDto = new LogInResponseDto();

        Pair<User, MultiValueMap<String, String>> userWithHeaders = authService.logIn(logInRequestDto.getEmail(), logInRequestDto.getPassword());
        User user = userWithHeaders.a;
        responseDto.setUsername(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setMessage("User Logged In Successfully");
        return new ResponseEntity<>(responseDto, userWithHeaders.b, HttpStatus.OK);
    }

    @PostMapping("/validateTokenByUserId")
    public ResponseEntity<ValidateTokenResponseDto> validate(@RequestBody ValidateTokenRequestDto validateTokenDto) {
        ValidateTokenResponseDto responseDto = new ValidateTokenResponseDto();
        System.out.println(validateTokenDto.getToken());
        Boolean response = authService.validateTokenByUserId(validateTokenDto.getToken(), validateTokenDto.getUserId());
        if (response) {
            responseDto.setMessage("Token Validated Successfully");
        } else {
            throw new InvalidTokenException("Invalid Token");
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @GetMapping("/validateToken/")
    public UserDto validateToken(@RequestParam String token) {
        System.out.println(token + "token to validate");
        User user = authService.validateToken(token);
        if (user == null)
            return null;
        return from(user);
    }

        @PostMapping("/logout")
        public ResponseEntity<LogOutResponseDto> logOut (@RequestBody LogOutRequestDto logOutRequestDto){
            LogOutResponseDto responseDto = new LogOutResponseDto();

            String message = authService.logout(logOutRequestDto.getEmail());
            responseDto.setMessage(message);

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        @GetMapping("/login/google")
        public void redirectToGoogleLogin (HttpServletResponse response) throws IOException {
            String redirectUrl = "http://localhost:9000/oauth2/authorization/google";
            response.sendRedirect(redirectUrl);
        }

        @GetMapping("/oauth2/success")
        public ResponseEntity<LogInResponseDto> oauth2Success (
                @RequestParam String token,
                @RequestParam String name,
                @RequestParam String email
        ){
            LogInResponseDto dto = new LogInResponseDto();
            dto.setMessage("Google login successful");
            dto.setUsername(name);
            dto.setEmail(email);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            return new ResponseEntity<>(dto, headers, HttpStatus.OK);
        }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}

