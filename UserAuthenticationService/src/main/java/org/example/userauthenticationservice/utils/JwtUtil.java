package org.example.userauthenticationservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.userauthenticationservice.models.Role;
import org.example.userauthenticationservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
@Component

public class JwtUtil {

    @Autowired
    private SecretKey jwtSecretKey;

    public String generateToken(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getValue)
                .toList();

        // ✅ Generate JWT token
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("user_id",user.getId());
        claims.put("name",user.getName());
        claims.put("email",user.getEmail());
        claims.put("roles", roleNames);
        long nowInMills =System.currentTimeMillis();
        claims.put("issued_at",nowInMills);
        claims.put("expiry_at",nowInMills+7200000);
        System.out.println(Base64.getEncoder().encodeToString(jwtSecretKey.getEncoded()));
        return  Jwts.builder().claims(claims).signWith(jwtSecretKey, Jwts.SIG.HS256).compact();
    }
}
