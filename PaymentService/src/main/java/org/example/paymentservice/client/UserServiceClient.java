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
        String url = "http://userService/auth/validateToken/?token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        ResponseEntity<UserDto> response = restTemplate.exchange(url , HttpMethod.GET, new HttpEntity<>(headers), UserDto.class);
        if(!response.hasBody()){
            return null;
        }
        return  response.getBody();
    }

    public boolean updateOrder(OrderRequestDto requestDto){
        try {
            String url = "http://orderService/orders/update" ;
            HttpHeaders headers = new HttpHeaders();
            //headers.set("Authorization", "Bearer " + internalToken);
            headers.setContentType(MediaType.APPLICATION_JSON); // important if sending JSON
            HttpEntity<OrderRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);
            ResponseEntity<Void> response = restTemplate.exchange(url , HttpMethod.PUT, requestEntity, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        }catch (RestClientException e){
            e.printStackTrace();
            return false;
        }

    }
}
