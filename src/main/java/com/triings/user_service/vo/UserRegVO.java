package com.triings.user_service.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.triings.user_service.enums.LoginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 3699513854758614600L;

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name cannot exceed 50 character")
    private String firstname;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name cannot exceed 50 character")
    private String lastname;
    @Pattern(regexp = "^(?!\\d)(?=.*[a-zA-Z])[a-zA-Z]+[a-zA-Z0-9._]*[a-zA-Z0-9]*[._]*$",
            message = "Allowed character for the username are :A-z,a-z,0-9 and ._ and\" +\n" +
                    "            \" Username should start with a letter")
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Mobile cannot be empty")
    private String mobile;
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email ID")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @JsonIgnore
    private String password;
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "Invalid gender")
    private String gender;
    @NotNull(message = "Account type cannot be empty")
    @Pattern(regexp = "^(BUSINESS|INDIVIDUAL|أعمال|فرد)$", message = "Invalid account type provided")
    private String accountType;
    private Long dob;
    @Size(max = 500, message = "Bio cannot exceed 500 character")
    private String bio;
    private String profilePictureUrl;
    private String coverImageUrl;
    private Integer cityId;
    @NotNull(message = "Country id cannot be empty")
    private Long countryId;
    private String role;
    private String location;
    @NotNull(message = "Login type is mandatory")
    @Enumerated(EnumType.STRING)
    @Pattern(regexp = "^(EMAIL|MOBILE)$", message = "Invalid login type provided")
    private String loginType;
    private String address;
    private String website;
    private Long platformId;
    private String platformPassword;
}
