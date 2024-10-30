package org.example.userauthenticationservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.models.Session;
import org.example.userauthenticationservice.models.SessionState;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repositories.SessionRepo;
import org.example.userauthenticationservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SessionRepo sessionRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SecretKey secretKey;

    public User signUp(String email,String password){
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent()){
            return null;
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(newUser);
        return newUser;
    }

    public Pair<User, MultiValueMap<String, String>> logIn(String email, String password){
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(!optionalUser.isPresent()){
            return null;
        }
        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            return null;
        }
        //Token Generation

//        String message = "{\n" +
//                "   \"email\": \"anurag@scaler.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"25thJuly2024\"\n" +
//                "}";

        HashMap<String,Object> claims = new HashMap<>();
        claims.put("user_id",user.getId());
        claims.put("email",user.getEmail());
        claims.put("roles",user.getRoleSet());
        long nowInMills =System.currentTimeMillis();
        claims.put("iat",nowInMills);
        claims.put("expiry",nowInMills+1000000);

//        byte[] content = message.getBytes(StandardCharsets.UTF_8);
//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();
//        String token = Jwts.builder().content(content).signWith(secretKey).compact();

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);

        Session session = new Session();
        session.setSessionState(SessionState.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        sessionRepo.save(session);

        return new Pair<User,MultiValueMap<String,String>>(user,headers);
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        //first check token is valid
        Optional<Session> optionalSession = sessionRepo.findByToken(token);
        if(optionalSession.isEmpty())
            return false;
        Session session = optionalSession.get();
        String dbtoken = session.getToken();

        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(dbtoken).getPayload();

        Long tokenExpiry = (Long)claims.get("expiry");

        Long currentTime = System.currentTimeMillis();

        System.out.println(tokenExpiry);
        System.out.println(currentTime);

        if(currentTime > tokenExpiry) {
            System.out.println(
                    "Token is expired");
            //set state to expired in my DB
            return false;
        }
        //Below we can validate other fields as well from claims

        User user = userRepo.findById(userId).get();
        String email = user.getEmail();
        String tokenEmail = (String)claims.get("email");
        if(!email.equals(tokenEmail)) {
            System.out.println("email mismatch");
            return false;
        }

        return true;
    }


}
