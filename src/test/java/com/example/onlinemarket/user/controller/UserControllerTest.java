package com.example.onlinemarket.user.controller;

import static com.example.onlinemarket.domain.user.constants.SessionKey.LOGGED_IN_USER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.common.exception.DuplicatedException;
import com.example.onlinemarket.common.exception.NotFoundException;
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
import org.springframework.mock.web.MockHttpSession;
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
    MockHttpSession mockHttpSession;

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
        mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(LOGGED_IN_USER, 1);
    }

    @Test
    @DisplayName("회원가입에 요청이 올바르면 201 Created 상태코드를 반환하고 회원가입에 성공한다.")
    void signUp_Success() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders

                .post("/users")
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
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));

        actions
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공하면 200 Ok를 반환한다.")
    void login_Success() throws Exception {
        given(userService.findLoggedInUser(loginRequest.getEmail(),
                loginRequest.getPassword())).willReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        Mockito.verify(loginService).login(userDTO.getId());
    }

    @Test
    @DisplayName("로그인 실패하면 404 Not Found를 반환한다.")
    void login_Fail_Wrong_Credentials() throws Exception {
        given(userService.findLoggedInUser(loginRequest.getEmail(),
                loginRequest.getPassword())).willThrow(new NotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound());

        Mockito.verify(loginService, Mockito.never()).login((int) anyLong());
    }

    @Test
    @DisplayName("로그아웃을 성공하면 200 Ok를 반환한다.")
    void logout_Success() throws Exception {
        willDoNothing().given(loginService).logout();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isOk());

        Mockito.verify(loginService).logout();
    }
}
