package com.triings.user_service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OTPResponseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2264711732361680167L;

    private String response;
}
