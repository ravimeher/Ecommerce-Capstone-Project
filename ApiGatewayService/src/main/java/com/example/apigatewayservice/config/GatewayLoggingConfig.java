package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class GatewayLoggingConfig {
    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            ServerWebExchange request = exchange;
            System.out.println("🌐 Incoming request: " + request.getRequest().getURI());
            return chain.filter(exchange);
        };

    }
}