package org.example.userauthenticationservice.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.userauthenticationservice.services.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class SpringSecurityConfig {

//    @Bean
//    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.cors().disable();
//        httpSecurity.csrf().disable();
//        httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//
//        return httpSecurity.build();
//    }
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",          // Allow all your auth-related endpoints
                                "/user/**",         // Allow all your user-related endpoints
                                "/oauth2/**",      // Needed for Google login redirect URI
                                "/login/**"       // Google OAuth2 callback URL
                        ).permitAll()
                        .anyRequest().permitAll() // For now, allow everything (Gateway will protect later .anyRequest().authenticated())
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login/google") // Only hit this when you explicitly want Google login
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(successHandler()) // Return JWT directly after login
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                org.springframework.security.core.Authentication authentication)
                    throws IOException, ServletException {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String jwt = (String) oAuth2User.getAttributes().get("jwt");
                String name = URLEncoder.encode((String) oAuth2User.getAttributes().get("name"), StandardCharsets.UTF_8);
                String email = URLEncoder.encode((String) oAuth2User.getAttributes().get("email"), StandardCharsets.UTF_8);

                String redirectUrl = String.format("https://6ad12d823aa4.ngrok-free.app/auth/oauth2/success?token=%s&name=%s&email=%s", jwt, name, email);
                response.sendRedirect(redirectUrl);
            }
        };
    }

}

