package org.example.userauthenticationservice.services;

import io.jsonwebtoken.Jwts;
import org.example.userauthenticationservice.exceptions.SessionAlreadyExistsException;
import org.example.userauthenticationservice.models.Role;
import org.example.userauthenticationservice.models.Session;
import org.example.userauthenticationservice.models.SessionState;
import org.example.userauthenticationservice.models.User;
import org.example.userauthenticationservice.repositories.RoleRepo;
import org.example.userauthenticationservice.repositories.SessionRepo;
import org.example.userauthenticationservice.repositories.UserRepo;
import org.example.userauthenticationservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

//http://localhost:9000/oauth2/authorization/google

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SessionRepo sessionRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAuthProvider("GOOGLE");

            // Add default role
            Role userRole = roleRepository.findByValue("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            user.setRoles(List.of(userRole));

            user  = userRepository.save(user);
        }
        //See if user already logged in
        Optional<Session> optionalExistingSession = sessionRepo.findByUserAndSessionState(user, SessionState.ACTIVE);
        if(optionalExistingSession.isPresent()) {
            throw new SessionAlreadyExistsException("User already has an active session");
        }

        String token = jwtUtil.generateToken(user);

        Session session = new Session();
        session.setSessionState(SessionState.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        sessionRepo.save(session);

        // Optional: Add JWT as attribute
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("jwt", token);
        attributes.put("name",user.getName());
        attributes.put("email",user.getEmail());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "sub"
        );
    }
}


