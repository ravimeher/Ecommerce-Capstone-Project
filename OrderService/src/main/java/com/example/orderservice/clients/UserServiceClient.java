package com.example.orderservice.clients;


import com.example.orderservice.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userService")
public interface UserServiceClient {

    @GetMapping("/auth/validateToken")
    UserDto validateToken(@RequestParam String token);
}
