package com.triings.user_service.controller;

import com.triings.trringscommon.vo.PaginatedResponseVO;
import com.triings.trringscommon.vo.ResponseVO;
import com.triings.trringscommon.vo.UserVO;
import com.triings.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService; // Mock the UserService

    @InjectMocks
    private UserController userController; // Inject the UserController

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testUserGetAll() throws Exception {
        // Prepare the mock response
        PaginatedResponseVO<UserVO> paginatedResponse = new PaginatedResponseVO<>();
        ResponseVO<PaginatedResponseVO<UserVO>> responseVO = new ResponseVO<>("SUCCESS", HttpStatus.OK.name(), paginatedResponse);

        // Mock the service call
        when(userService.getAll(0, 10)).thenReturn(paginatedResponse);

        // Perform the GET request and assert the response
        mockMvc.perform(get("/get-all")
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk()) // Expect HTTP 200 status
                .andExpect(content().json("{'status':'SUCCESS','code':'OK','data':{'data':[],'pageNumber':0,'pageSize':10,'totalElements':0}}"));
    }
}