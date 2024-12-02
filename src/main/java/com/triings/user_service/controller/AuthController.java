package com.triings.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triings.trringscommon.vo.ResponseVO;
import com.triings.user_service.service.AuthService;
import com.triings.user_service.vo.AuthRequest;
import com.triings.user_service.vo.UserRegVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.triings.trringscommon.exception.ErrorCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @PostMapping("/register")
    public ResponseVO<Map<String,String>> register(@RequestParam(value = "profile",required = false) MultipartFile profile,
                                                   @Valid @RequestPart(value = "data") UserRegVO userDetails) throws IOException {
        return new ResponseVO<>("SUCCESS", HttpStatus.OK.name(), authService.register(userDetails, profile));
    }

    @PostMapping("/login")
    public ResponseVO<Map<String,String>> login(@RequestBody AuthRequest loginRequest) {
        return new ResponseVO<>(SUCCESS, HttpStatus.OK.name(), authService.userAuth(loginRequest));
    }

}
