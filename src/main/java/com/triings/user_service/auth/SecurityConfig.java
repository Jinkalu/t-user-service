package com.triings.user_service.auth;


import com.triings.trringscommon.filter.JWTAuthenticationFilter;
import com.triings.trringscommon.repository.UsersRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends com.triings.trringscommon.config.SecurityConfig{
    public SecurityConfig(UsersRepository repository, JWTAuthenticationFilter jwtAuthFilter) {
        super(repository, jwtAuthFilter);
    }
}
