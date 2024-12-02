package com.triings.user_service.service.impl;

import com.triings.trringscommon.entity.Role;
import com.triings.trringscommon.exception.ApiError;
import com.triings.trringscommon.exception.ValidationException;
import com.triings.user_service.repository.RoleRepository;
import com.triings.user_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String role) {
        return roleRepository.findByNameAndIsEnabled(role,true)
                .orElseThrow(() -> new ValidationException(ApiError.builder()
                        .code(BAD_REQUEST.name())
                        .status(BAD_REQUEST.name())
                        .httpStatus(BAD_REQUEST)
                        .errors(List.of("Role not found"))
                        .build()));
    }
}
