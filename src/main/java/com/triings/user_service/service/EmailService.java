package com.triings.user_service.service;

import com.triings.user_service.vo.OTPResponseVO;

public interface EmailService {
    OTPResponseVO sendOTP(String lowerCase);
}
