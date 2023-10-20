package com.example.dashboard.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

     @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(  "/api/**","/api/accounts/signup","/api/accounts/signin","/swagger-ui/**","/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/accounts/{Id}","/api/transfer").permitAll()
                .anyRequest().authenticated()
            );
            
        return http.build();  
    }
}
