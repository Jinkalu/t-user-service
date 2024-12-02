package com.triings.user_service.service.impl;

import com.triings.trringscommon.entity.Users;
import com.triings.trringscommon.enums.UserStatus;
import com.triings.trringscommon.vo.PaginatedResponseVO;
import com.triings.trringscommon.vo.UserVO;
import com.triings.user_service.repository.UsersRepository;
import com.triings.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.triings.user_service.service.impl.mapper.UserMapper.mapToUserVO;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    @Override
    public PaginatedResponseVO<UserVO> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Users> users = usersRepository.findByStatus(UserStatus.ACTIVE, pageable);
        List<UserVO> response = new ArrayList<>();
        if (!users.isEmpty()) {
            users.forEach(user -> response.add(mapToUserVO(user)));
        }
        return new PaginatedResponseVO<>(response, users.getTotalElements(), pageNumber, pageSize,
                users.getTotalPages(), users.isLast());
    }

}
