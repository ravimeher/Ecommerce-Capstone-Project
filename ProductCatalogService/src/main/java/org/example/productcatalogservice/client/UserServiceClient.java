package org.example.productcatalogservice.client;

import org.example.productcatalogservice.dto.UserDto;
import org.example.productcatalogservice.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {
    @Autowired
    private RestTemplate restTemplate;

    public UserDto validateToken(String tokenValue){

        String token = tokenValue.replace("Bearer ", "").trim();
        String url = "http://userService/auth/validateToken/?token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        ResponseEntity<UserDto> response = restTemplate.exchange(url , HttpMethod.GET, new HttpEntity<>(headers), UserDto.class);
        if(!response.hasBody()){
            return null;
        }
        return  response.getBody();
    }

    public boolean hasRequiredRole(UserDto user, String requiredRole) {
        System.out.println(user.getRoles() +"ROLES");
        return user != null && user.getRoles().stream().map(Role::getValue).anyMatch(role ->role.equalsIgnoreCase(requiredRole) );
    }
}
