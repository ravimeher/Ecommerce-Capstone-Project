package com.example.orderservice.clients;

import com.example.orderservice.dtos.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "productService")
public interface ProductServiceClient {

    @GetMapping("/products/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") long id);
}
