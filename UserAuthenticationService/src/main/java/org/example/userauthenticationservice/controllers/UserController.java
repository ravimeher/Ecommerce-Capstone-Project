package org.example.userauthenticationservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.QueryParam;
import org.example.userauthenticationservice.dtos.*;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws JsonProcessingException {
        SignUpResponseDto responseDto = new SignUpResponseDto();

        User user = userService.signUp(signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword(),
                signUpRequestDto.getRoles());

        responseDto.setEmail(user.getEmail());
        responseDto.setName(user.getName());
        responseDto.setRoles(user.getRoles());
        responseDto.setMessage("User Created Successfully");

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }


    @GetMapping("/{uid}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long uid){
        User user = userService.getUser(uid);
        return new ResponseEntity<>(from(user), HttpStatus.OK);
    }

    //forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponseDto> forgotPassword(@RequestBody ForgotPasswordRequestDto forgotPasswordDto) throws JsonProcessingException {
        ForgotPasswordResponseDto responseDto = new ForgotPasswordResponseDto();
        String message = userService.forgotPassword(forgotPasswordDto.getEmail());
        responseDto.setMessage(message);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponseDto> resetPasswordRedirect(@QueryParam("token")String token){

        ResetPasswordRequestDto resetPasswordRequestDto = new ResetPasswordRequestDto();
        resetPasswordRequestDto.setToken(token);
        resetPasswordRequestDto.setNewPassword(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        ResponseEntity<ResetPasswordResponseDto> response = resetPassword(resetPasswordRequestDto);
        return response;
    }

    @PostMapping("/Reset-password")
    public ResponseEntity<ResetPasswordResponseDto> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        ResetPasswordResponseDto responseDto = new ResetPasswordResponseDto();
        String message = userService.resetPassword(resetPasswordRequestDto.getToken(),resetPasswordRequestDto.getNewPassword());
        responseDto.setMessage(message);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    //edit details
    @PostMapping("/edit")
    public ResponseEntity<UserDto> editUser(@RequestBody UserDto userDto){
        UserDto responseDto = new UserDto();
        User user = userService.editUserInfo(userDto.getName(), userDto.getEmail(), userDto.getRoles());
        responseDto.setEmail(user.getEmail());
        responseDto.setName(userDto.getName());
        responseDto.setRoles(user.getRoles());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    private UserDto from (User user){
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
