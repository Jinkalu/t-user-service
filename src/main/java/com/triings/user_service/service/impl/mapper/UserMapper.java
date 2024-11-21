package com.triings.user_service.service.impl.mapper;

import com.triings.trringscommon.entity.Country;
import com.triings.trringscommon.entity.Role;
import com.triings.trringscommon.entity.Users;
import com.triings.trringscommon.enums.AccountType;
import com.triings.trringscommon.enums.Gender;
import com.triings.trringscommon.vo.UserVO;
import com.triings.user_service.vo.UserRegVO;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.triings.trringscommon.enums.UserStatus.ACTIVE;
import static com.triings.trringscommon.enums.UserStatus.UNREGISTERED;

public class UserMapper {

    public static Users mapAsNewUser(String email, String password, Role role) {
        return Users.builder()
                .email(email)
                .password(password)
                .passwordUpdatedAt(Instant.now().toEpochMilli())
                .status(UNREGISTERED)
                .firstname("unknown")
                .lastname("unknown")
                .userRole(role)
                .userUid(UUID.randomUUID())
                .isSocialAccount(false)
                .build();
    }

    public static Users getRegisteredUser(UserRegVO userDetailsVO, Users user,
                                          Role userRole, Country country, String profile) {
        return Users.builder()
                .id(user.getId())
                .userUid(user.getUserUid())
                .firstname(userDetailsVO.getFirstname())
                .lastname(userDetailsVO.getLastname())
                .username(userDetailsVO.getUsername().trim().toLowerCase())
                .profilePicture(profile)
                .accountType(AccountType.valueOf(userDetailsVO.getAccountType()))
                .country(country)
                .mobile(userDetailsVO.getMobile().trim())
                .email(user.getEmail())
                .dob(Objects.requireNonNullElse(userDetailsVO.getDob(), 0L))
                .bio(Objects.requireNonNullElse(userDetailsVO.getBio(), ""))
                .gender(Objects.isNull(userDetailsVO.getGender()) ? null : Gender.valueOf(userDetailsVO.getGender()))
                .userRole(userRole)
                .isVerified(false)
                .status(ACTIVE)
                .address(!StringUtils.isEmpty(userDetailsVO.getAddress()) ? userDetailsVO.getAddress() : null)
                .website(!StringUtils.isEmpty(userDetailsVO.getWebsite()) ? userDetailsVO.getWebsite() : null)
                .build();
    }

    public static UserVO mapToUserVO(Users user) {
        return UserVO.builder()
                .id(user.getId())
                .userUid(user.getUserUid().toString())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .gender(user.getGender().name())
                .isVerified(user.getIsVerified())
                .bio(StringUtils.isEmpty(user.getBio()) ? user.getBio() : null)
                .status((short) user.getStatus().ordinal())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
