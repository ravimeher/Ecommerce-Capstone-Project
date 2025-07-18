package org.example.paymentservice.client;

import org.example.paymentservice.dtos.OrderRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderServiceClient {
    @Autowired
    private RestTemplate restTemplate;

    public boolean updateOrder(OrderRequestDto requestDto) {
        try {
            String url = "http://orderService/orders/update";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<OrderRequestDto> requestEntity = new HttpEntity<>(requestDto);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
