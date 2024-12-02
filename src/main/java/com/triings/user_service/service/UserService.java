package com.triings.user_service.service;

import com.triings.trringscommon.vo.PaginatedResponseVO;
import com.triings.trringscommon.vo.UserVO;

public interface UserService {

    PaginatedResponseVO<UserVO> getAll(int pageNumber, int pageSize);
}
