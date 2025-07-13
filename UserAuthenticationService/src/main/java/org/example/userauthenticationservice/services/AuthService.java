package org.example.userauthenticationservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice.dtos.SendEmailDto;
import org.example.userauthenticationservice.exceptions.*;
import org.example.userauthenticationservice.models.Role;
import org.example.userauthenticationservice.models.Session;
import org.example.userauthenticationservice.models.SessionState;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repositories.RoleRepo;
import org.example.userauthenticationservice.repositories.SessionRepo;
import org.example.userauthenticationservice.repositories.UserRepo;
import org.example.userauthenticationservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import javax.management.relation.InvalidRoleInfoException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SessionRepo sessionRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SecretKey jwtSecretKey;
    @Autowired
    private JwtUtil jwtUtil;
//    @Autowired
//    private KafkaProducerClient kafkaProducerClient;
    @Autowired
    private ObjectMapper objectMapper;



    public Pair<User, MultiValueMap<String, String>> logIn(String email, String password){
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw new UserNotFoundException("User not found");
        }
        User user = optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new InvalidPasswordException("Enter Valid Password");
        }
        //See if user already logged in
        Optional<Session> optionalExistingSession = sessionRepo.findByUserAndSessionState(user,SessionState.ACTIVE);
        if(optionalExistingSession.isPresent()){
            throw new SessionAlreadyExistsException("User already has an active session");
        }

        //Token Generation
        String token = jwtUtil.generateToken(user);

        //sending token and secret used as headers in response
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("token",token);
        headers.add("Secret_Used", Base64.getEncoder().encodeToString(jwtSecretKey.getEncoded()));

        Session session = new Session();
        session.setSessionState(SessionState.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        sessionRepo.save(session);

        return new Pair<User,MultiValueMap<String,String>>(user,headers);
    }

    public String logout(String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Enter Correct Email"));
        Optional<Session> optionalSession = sessionRepo.findByUserAndSessionState(user,SessionState.ACTIVE);
        Session session = optionalSession.get();
        if(session.getSessionState().equals(SessionState.ACTIVE)){
            session.setSessionState(SessionState.EXPIRED);
        }
        sessionRepo.save(session);
        optionalSession = sessionRepo.findByUserAndSessionState(user,SessionState.ACTIVE);

        if(optionalSession.isEmpty()){
            return "Successfully Logged Out";
        }else {
            throw new RuntimeException("Unable to Log Out");
        }
    }

    public Boolean validateTokenByUserId(String token, Long userId) {
        //first check token is valid
        Optional<Session> optionalSession = sessionRepo.findByToken(token);
        if(optionalSession.isEmpty())
            throw new InvalidTokenException("Invalid Token");

        Session session = optionalSession.get();
        String dbtoken = session.getToken();
        System.out.println(Base64.getEncoder().encodeToString(jwtSecretKey.getEncoded()));

        //send secret and validate
        JwtParser jwtParser = Jwts.parser().verifyWith(jwtSecretKey).build();
        Claims claims = jwtParser.parseSignedClaims(dbtoken).getPayload();

        Long tokenExpiry = (Long)claims.get("expiry_at");

        Long currentTime = System.currentTimeMillis();

        System.out.println(tokenExpiry);
        System.out.println(currentTime);

        if(currentTime > tokenExpiry) {
            System.out.println(
                    "Token is expired");
            //set state to expired in my DB
            session.setSessionState(SessionState.EXPIRED);
            sessionRepo.save(session);
            throw new InvalidTokenException("Token is expired");
        }

        //Below we can validate other fields as well from claims

        User user = userRepo.findById(userId).get();
        String email = user.getEmail();
        String tokenEmail = (String)claims.get("email");
        if(!email.equals(tokenEmail)) {
            System.out.println("email mismatch");
            throw new InvalidTokenException("email mismatch with user and token");
        }

        return true;
    }

    public User validateToken(String token) {
        //first check token is valid
        Optional<Session> optionalSession = sessionRepo.findByToken(token);
        if(optionalSession.isEmpty())
            throw new InvalidTokenException("Invalid Token");

        Session session = optionalSession.get();
        String dbtoken = session.getToken();
        System.out.println(Base64.getEncoder().encodeToString(jwtSecretKey.getEncoded()));

        //send secret and validate
        JwtParser jwtParser = Jwts.parser().verifyWith(jwtSecretKey).build();
        Claims claims = jwtParser.parseSignedClaims(dbtoken).getPayload();

        Long tokenExpiry = (Long)claims.get("expiry_at");

        Long currentTime = System.currentTimeMillis();

        System.out.println(tokenExpiry);
        System.out.println(currentTime);

        if(currentTime > tokenExpiry) {
            System.out.println(
                    "Token is expired");
            //set state to expired in my DB
            session.setSessionState(SessionState.EXPIRED);
            sessionRepo.save(session);
            throw new InvalidTokenException("Token is expired");
        }
        return session.getUser();
    }


//    public String generateRefreshToken(String token) {
//        Session session = sessionRepo.findByToken(token).get();
//        User user = session.getUser();
//
//        Map<String,Object> claims = new HashMap<>();
//        claims.put("user_id__",user.getId());
//        claims.put("roles",user.getRoles());
//        claims.put("email",user.getEmail());
//        long nowInMillis = System.currentTimeMillis();
//        claims.put("iat",nowInMillis);
//        claims.put("exp",nowInMillis+1000000);
//
//
//    }
}
