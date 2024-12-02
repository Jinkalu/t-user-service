package com.triings.user_service.repository;

import com.triings.trringscommon.entity.Users;
import com.triings.trringscommon.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends com.triings.trringscommon.repository.UsersRepository {
    Page<Users> findByStatus(UserStatus status, Pageable pageable);
}
