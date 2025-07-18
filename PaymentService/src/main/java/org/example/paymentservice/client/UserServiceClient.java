package org.example.paymentservice.client;

import org.example.paymentservice.dtos.OrderRequestDto;
import org.example.paymentservice.dtos.UserDto;
import org.example.paymentservice.dtos.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {
    @Autowired
    private RestTemplate restTemplate;

    public UserDto validateToken(String tokenValue){

        String token = tokenValue.replace("Bearer ", "").trim();
        String url = "http://userService/auth/validateToken?token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        ResponseEntity<UserDto> response = restTemplate.exchange(url , HttpMethod.GET, new HttpEntity<>(headers), UserDto.class);
        if(!response.hasBody()){
            return null;
        }
        return  response.getBody();
    }

}
