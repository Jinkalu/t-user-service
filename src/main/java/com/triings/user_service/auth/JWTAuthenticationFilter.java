package com.triings.user_service.auth;

import com.triings.trringscommon.repository.UsersRepository;
import com.triings.trringscommon.service.JWTAuthenticationService;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationFilter extends com.triings.trringscommon.filter.JWTAuthenticationFilter{
    public JWTAuthenticationFilter(JWTAuthenticationService jwtAuthenticationService, UsersRepository usersRepository) {
        super(jwtAuthenticationService, usersRepository);
    }
}
