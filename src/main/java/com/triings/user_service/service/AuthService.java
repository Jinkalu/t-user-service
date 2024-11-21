package com.triings.user_service.service;

import com.triings.user_service.vo.AuthRequest;
import com.triings.user_service.vo.UserRegVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface AuthService {

    Map<String, String> userAuth(AuthRequest request);

    Map<String, String> register(UserRegVO userDetails, MultipartFile profile) throws IOException;
}
