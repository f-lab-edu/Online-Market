package com.example.onlinemarket.user.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.common.exception.DuplicatedException;
import com.example.onlinemarket.domain.user.controller.UserController;
import com.example.onlinemarket.domain.user.dto.SignUpRequest;
import com.example.onlinemarket.domain.user.dto.UserDTO;
import com.example.onlinemarket.domain.user.service.LoginService;
import com.example.onlinemarket.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    @MockBean
    LoginService loginService;
    UserDTO userDTO;
    SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest("test1234@example.com", "test1234", "geonhui",
                "01012345678");
        loginRequest = new LoginRequest("test1234@example.com", "test1234");
        userDTO = UserDTO.builder()
                .id(1)
                .email("test1234@example.com")
                .password("test1234")
                .name("geonhui")
                .phone("01012345678")
                .build();
    }

    @Test
    @DisplayName("회원가입에 요청이 올바르면 201 Created 상태코드를 반환하고 회원가입에 성공한다.")
    void signUp_Success() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders

                .post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        actions.andExpect(status().isCreated()).andDo(print());
        Mockito.verify(userService).signUp(signUpRequest);
    }

    @Test
    @DisplayName("이메일이 중복되면 409 Conflict를 반환하고 회원가입에 실패한다.")
    void signUp_Fail_Duplicate_Email() throws Exception {
        doThrow(new DuplicatedException("중복된 이메일입니다.")).when(userService).signUp(signUpRequest);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        actions
                .andExpect(status().isConflict());
    }
}