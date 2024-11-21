package com.triings.user_service.service;

import com.triings.trringscommon.entity.Users;
import com.triings.trringscommon.enums.TokenType;

public interface TokenService {

    void saveToken(Users userDetails, String token, TokenType tokenName, long sessionId);
}
