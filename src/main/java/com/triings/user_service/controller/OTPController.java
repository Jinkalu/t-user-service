package com.triings.user_service.controller;

import com.triings.trringscommon.vo.ResponseVO;
import com.triings.user_service.service.EmailService;
import com.triings.user_service.vo.OTPResponseVO;
import com.triings.user_service.vo.OtpVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/v1/otp")
public class OTPController {

    private final EmailService  emailService;

    @PostMapping("/send-otp")
    public ResponseVO<OTPResponseVO> sendOTP(@RequestBody OtpVO otpRequest) {
        return new ResponseVO<>("SUCCESS", HttpStatus.OK.name(), emailService.sendOTP(otpRequest.getEmail().trim().toLowerCase()));
    }
}
