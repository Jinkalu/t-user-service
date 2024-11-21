package com.triings.user_service.service.impl;

import com.triings.trringscommon.entity.Role;
import com.triings.trringscommon.entity.Users;
import com.triings.trringscommon.exception.BadRequestException;
import com.triings.user_service.repository.RoleRepository;
import com.triings.user_service.repository.UsersRepository;
import com.triings.user_service.service.EmailService;
import com.triings.user_service.service.impl.mapper.UserMapper;
import com.triings.user_service.vo.OTPResponseVO;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Instant;
import java.util.Random;

import static com.triings.trringscommon.enums.UserStatus.DELETED;
import static com.triings.trringscommon.exception.ErrorCode.INVALID_REQUEST_PARAM;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final UsersRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final SpringTemplateEngine templateEngine;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public OTPResponseVO sendOTP(String email) {
        return userRepository.findByEmail(email)
                .map(user -> handleExistingUser(user, email))
                .orElseGet(() -> addNewUser(email));
    }

    @Transactional(rollbackOn = {Exception.class})
    private OTPResponseVO handleExistingUser(Users user, String email) {
        if (user.getStatus().equals(DELETED)) {
            throw new BadRequestException(INVALID_REQUEST_PARAM,
                    "Your account has been deleted, please contact the administrator");
        }
        String otp = sendEmail(email);
        if (!StringUtils.isEmpty(otp)) {
            user.setPassword(passwordEncoder.encode(otp));
            user.setPasswordUpdatedAt(Instant.now().toEpochMilli());
            userRepository.save(user);
            return OTPResponseVO.builder().response("OTP sent successfully.").build();
        }
        throw new BadRequestException("EMAIL_SEND_FAILURE", "Failed to send OTP.");
    }

    @Transactional(rollbackOn = {Exception.class})
    private OTPResponseVO addNewUser(String email) {
        String res = sendEmail(email);
        if (!StringUtils.isEmpty(res)) {
            Role role = roleRepository.findByNameAndIsEnabled("ROLE_USER", true)
                    .orElseThrow();
            userRepository.save(UserMapper.mapAsNewUser(email, passwordEncoder.encode(res), role));
        }
        return OTPResponseVO.builder().response("OTP sent successfully.").build();
    }

    @Transactional(rollbackOn = {Exception.class})
    public String sendEmail(String email) {
        String OTP = generateOTP();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            Context context = new Context();
            context.setVariable("otp", OTP);
            helper.setFrom("noreply@pangeanis.com");
            helper.setTo(email);
            helper.setSubject("Pangeanis Team");
            String htmlContent = templateEngine.process("index", context);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            return OTP;
        } catch (Exception e) {
            throw new BadRequestException("EMAIL_SEND_FAILURE", "Failed to send OTP.");
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int OTP = random.nextInt(999999);
        return String.format("%06d", OTP);
    }
}
