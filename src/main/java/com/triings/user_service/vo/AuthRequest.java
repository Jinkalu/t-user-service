package com.triings.user_service.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2479409055178006321L;
    private String email;
    private String password;
}
