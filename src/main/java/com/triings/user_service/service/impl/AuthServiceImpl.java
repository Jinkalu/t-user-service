package com.triings.user_service.service.impl;

import com.triings.trringscommon.entity.Country;
import com.triings.trringscommon.entity.Role;
import com.triings.trringscommon.entity.Users;
import com.triings.trringscommon.enums.TokenType;
import com.triings.trringscommon.exception.ApiError;
import com.triings.trringscommon.exception.ValidationException;

import com.triings.trringscommon.utils.CommonUtils;
import com.triings.user_service.enums.LoginType;
import com.triings.user_service.repository.UsersRepository;
import com.triings.user_service.service.*;
import com.triings.user_service.service.impl.mapper.UserMapper;
import com.triings.user_service.vo.AuthRequest;
import com.triings.user_service.vo.UserRegVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.triings.trringscommon.exception.ErrorCode.INVALID_COUNTRY_ID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository repository;
    private final JWTAuthenticationService jwtService;
    private final TokenService tokenService;
    private final CountryService countryService;
    private final RoleService roleService;

//    private final AwsS3Service awsS3Service;

//    @Value("${application.bucket.url}")
//    private String bucketUrl;

    private static final String usernameRegEX = "^(?!\\d)(?=.*[a-zA-Z])[a-zA-Z]+[a-zA-Z0-9._]*[a-zA-Z0-9]*[._]*$";
    private static final String emailRegEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public Map<String, String> userAuth(AuthRequest request) {
        /*authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));*/
        Users user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException(buildApiError("User not found")));
        return switch (user.getStatus()) {
            case ACTIVE -> getTokens(user);
            case UNREGISTERED ->
                    throw new ValidationException(buildApiError("Please check your registration details"));
            default -> throw new ValidationException(buildApiError("User not found"));
        };
    }

    @Transactional(rollbackOn = {Exception.class})
    private Map<String, String> getTokens(Users user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        tokenService.saveToken(user, accessToken, TokenType.ACCESS_TOKEN, 1L);
        tokenService.saveToken(user, refreshToken, TokenType.REFRESH_TOKEN, 1L);
        return Map.of("accessToken", accessToken,
                "refreshToken", refreshToken);
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public Map<String, String> register(UserRegVO userDetailsVO, MultipartFile profilePic) throws IOException {
        userRegValidation(userDetailsVO);
        Users user = repository.findByEmail(userDetailsVO.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ValidationException(buildApiError("User not found")));
        Role userRole = roleService.getRoleByName(userDetailsVO.getRole());
        Country country = countryService.getCountryById(userDetailsVO.getCountryId());
        String profile = null;
        if (Objects.nonNull(profilePic) && !profilePic.isEmpty()) {
            validateImage(profilePic);
            HashMap<String, String> profileUploadResponse = uploadImage(profilePic);
            profile = profileUploadResponse.get("fileName :");
        }
        return getTokens(repository.save(UserMapper.getRegisteredUser(userDetailsVO, user, userRole, country, profile)));
    }

    private void userRegValidation(UserRegVO userRequest) {
        if (userRequest.getUsername().length() > 15) {
            throw new ValidationException(buildApiError("Username should contain 5 - 15 characters"));
        }
        if (!userRequest.getRole().equals("ROLE_ADMIN")) {
            if (userRequest.getUsername().length() < 5) {
                throw new ValidationException(buildApiError("Username should only contain 5 - 15 characters"));
            }
        }
        if (!userRequest.getUsername().isEmpty() && repository.existsByUsername(userRequest.getUsername().toLowerCase().trim())) {
            throw new ValidationException(buildApiError("Username already exists"));
        }
        if (!userRequest.getLoginType().equals(LoginType.MOBILE.name())) {
            if (!userRequest.getMobile().isEmpty() && repository.existsByMobile(userRequest.getMobile().trim())) {
                throw new ValidationException(buildApiError("Mobile number already exists"));
            }
        }
        if (!userRequest.getLoginType().equals(LoginType.EMAIL.name())) {
            if (!userRequest.getEmail().isEmpty() && repository.existsByEmail(userRequest.getEmail().trim())) {
                throw new ValidationException(buildApiError(
                        "This email has been already linked with another account"));
            }
        }
        if (!countryService.isCountryValid(userRequest.getCountryId())) {
            throw new ValidationException(INVALID_COUNTRY_ID, BAD_REQUEST, "Country not found");
        }
    }

    public static void validateImage(MultipartFile file) throws IOException {
        if (Objects.isNull(ImageIO.read(file.getInputStream()))) {
            throw new ValidationException(ApiError.builder()
                    .errors(List.of("File should be an image"))
                    .code(String.valueOf(BAD_REQUEST.value()))
                    .httpStatus(BAD_REQUEST)
                    .build());
        }
    }

    public HashMap<String, String> uploadImage(MultipartFile file) {
        HashMap<String, String> response = new HashMap<>();
        String newProfilePicName = CommonUtils.generateRandomString() + "_" + System.currentTimeMillis();
        MediaType mediaType = CommonUtils.getContentType(Objects.requireNonNull(file.getOriginalFilename()));
        if (Objects.isNull(mediaType)) {
            throw new ValidationException(ApiError.builder()
                    .httpStatus(BAD_REQUEST)
                    .code(String.valueOf(BAD_REQUEST.value()))
                    .status(BAD_REQUEST.name())
                    .errors(List.of("Invalid media format"))
                    .build());
        }
        String profileFileName = newProfilePicName + CommonUtils.getFileExtension(mediaType);
        //uploading to s3
//        awsS3Service.uploadFile(file, profileFileName);
        response.put("fileName :", profileFileName);
//        response.put("url :", bucketUrl + profileFileName);
        return response;
    }

    private ApiError buildApiError(String errorMessage) {
        return ApiError.builder()
                .code(BAD_REQUEST.name())
                .status(BAD_REQUEST.name())
                .httpStatus(BAD_REQUEST)
                .errors(List.of(errorMessage))
                .build();
    }

    private HashMap<String, Object> checkEmailAvailability(String email) {
        HashMap<String, Object> response = new HashMap<>();
        Pattern pattern = Pattern.compile(emailRegEX);
        if (pattern.matcher(email).matches()) {
            if (repository.existsByEmail(email.trim().toLowerCase())) {
                response.put("availability", false);
                response.put("message", "This email is linked with another user account");
            } else {
                response.put("availability", true);
                response.put("message", "Available");
            }
        } else {
            throw new ValidationException("INVALID_EMAIL", BAD_REQUEST,
                    "Invalid email format");
        }
        return response;
    }

    private HashMap<String, Object> checkUsernameAvailability(String username) {
        HashMap<String, Object> response = new HashMap<>();
        Pattern pattern = Pattern.compile(usernameRegEX);
        if (username.trim().length() < 5 || username.trim().length() > 15) {
            response.put("available", false);
            response.put("message", "Username can only contain 5 - 15 characters");
            return response;
        }
        if (pattern.matcher(username).matches()) {
            if (repository.existsByUsername(username.trim().toLowerCase())) {
                response.put("available", false);
                response.put("message", "Username not available");
            } else {
                response.put("available", true);
                response.put("message", "Username available");
            }
        } else {
            response.put("available", false);
            response.put("message", "Allowed character for the username are :A-z,a-z,0-9 and ._ and " +
                    "Username should start with a letter");
        }
        return response;
    }
}
