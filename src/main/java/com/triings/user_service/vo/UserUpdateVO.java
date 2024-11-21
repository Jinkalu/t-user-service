package com.triings.user_service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserUpdateVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 2796303379632399814L;

    private String userUid;
    private String firstname;
    private String lastname;
    private String address;
    private String bio;
    private String website;
    private String dob;
    private String dateOfEstablishment;
    private Long countryId;
}
