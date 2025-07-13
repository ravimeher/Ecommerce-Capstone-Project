package org.example.userauthenticationservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
@Configuration
public class AuthServiceConfig {

//    @Value("${jwt.secret}")
//    private String secret;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//        we can either use this or create a bean and send as secretKey if required in multiple places
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);
//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();
//        String token = Jwts.builder().content(content).signWith(secretKey).compact();

//    @Bean
//    public SecretKey generateSecret(){
//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();
//        return secretKey;
//    }
    @Bean
    public SecretKey jwtSecretKey(@Value("${jwt.secret}") String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

}
