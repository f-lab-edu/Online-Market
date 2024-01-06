package com.example.onlinemarket.user.controller;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.domain.user.controller.UserController;
import com.example.onlinemarket.domain.user.dto.LoginRequest;
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
    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("email@naver.com", "password", "name", "01011111111");
    }

    @Test
    @DisplayName("회원가입에 요청이 올바르면 201 Created 상태코드를 반환한다.")
    void signUp_Success() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders

                .post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        actions.andExpect(status().isCreated()).andDo(print());

        Mockito.verify(userService).signUp(refEq(userDTO));
    }

    @Test
    @DisplayName("이메일이 중복되면 409 BadRequest를  반환한다.")
    void signUp_Fail_Duplicate_Email() throws Exception {
        doThrow(new IllegalArgumentException()).when(userService).signUp(userDTO);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        actions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인을 성공하면 ok를 반환한다.")
    void login_Success() throws Exception {
        given(userService.checkLogin(loginRequest.getEmail(),
                loginRequest.getPassword())).willReturn(
                userDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        Mockito.verify(loginService).login(userDTO.getId());
    }
}