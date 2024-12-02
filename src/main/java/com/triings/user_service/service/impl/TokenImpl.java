package com.triings.user_service.service.impl;


import com.triings.trringscommon.entity.Token;
import com.triings.trringscommon.entity.Users;
import com.triings.trringscommon.enums.TokenType;
import com.triings.user_service.repository.TokenRepository;
import com.triings.user_service.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public void saveToken(Users userDetails, String token, TokenType tokenName, long sessionId) {
        Token tokenDetails = Token.builder()
                .user(userDetails)
                .token(token)
                .expired(false)
                .revoked(false)
                .tokenType(tokenName)
                .sessionId(sessionId)
                .build();
        tokenRepository.save(tokenDetails);
    }
}
