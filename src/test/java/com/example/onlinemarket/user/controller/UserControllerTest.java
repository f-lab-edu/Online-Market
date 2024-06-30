package com.example.onlinemarket.user.controller;

import static com.example.onlinemarket.domain.user.constants.SessionKey.LOGGED_IN_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinemarket.domain.user.controller.UserController;
import com.example.onlinemarket.domain.user.dto.UserDto;
import com.example.onlinemarket.domain.user.dto.request.LoginRequest;
import com.example.onlinemarket.domain.user.dto.request.SignUpRequest;
import com.example.onlinemarket.domain.user.exception.DuplicatedPhoneException;
import com.example.onlinemarket.domain.user.exception.DuplicatedUserIdException;
import com.example.onlinemarket.domain.user.exception.MisMatchPasswordException;
import com.example.onlinemarket.domain.user.exception.NotFoundUserIdException;
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
    UserDto user;
    SignUpRequest signUpRequest;
    LoginRequest loginRequest;
    MockHttpSession mockHttpSession;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest("1", "test1234@example.com", "test1234", "geonhui",
            "01012345678");
        loginRequest = new LoginRequest("test1234@example.com", "test1234");
        user = UserDto.builder()
            .email("test1234@example.com")
            .password("test1234")
            .name("geonhui")
            .phone("01012345678")
            .build();
        mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(LOGGED_IN_USER, 1);
    }

    @Test
    @DisplayName("회원가입에 요청이 올바르면 201 Created 상태코드를 반환한다.")
    void signUpSuccess() throws Exception {

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isCreated());

        // verify
        Mockito.verify(userService).signUp(signUpRequest);
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 실패하고 상태코드 409를 반환")
    void signUpFailureWithDuplicatedEmail() throws Exception {
        // given
        doThrow(new DuplicatedUserIdException("이미 존재하는 아이디입니다.")).when(userService).signUp(any(SignUpRequest.class));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isConflict());

        // verify
        Mockito.verify(userService).signUp(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("중복된 휴대폰 번호로 회원가입 실패하고 상태코드 409 반환")
    void signUpFailureWithDuplicatedPhoneNumber() throws Exception {
        // given
        doThrow(new DuplicatedPhoneException("중복된 휴대폰 번호입니다.")).when(userService).signUp(any(SignUpRequest.class));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isConflict());

        // verify
        Mockito.verify(userService).signUp(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("이메일 중복 체크 시 성공하여 상태코드 200 OK 반환")
    void checkEmailDuplicationSuccess() throws Exception {
        // given
        String userEmail = "test@example.com";
        doNothing().when(userService).checkUserIdDuplication(userEmail);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/check-duplication")
                .param("userEmail", userEmail))
            .andExpect(status().isOk());
    }


    @Test
    @DisplayName("이메일 중복 체크 시 중복된 이메일이 있으면 409 Conflict를 반환한다.")
    void checkUserEmailDuplicationFail() throws Exception {
        // given
        String userEmail = "test@example.com";
        doThrow(new DuplicatedUserIdException("이미 존재하는 아이디입니다.")).when(userService).checkUserIdDuplication(anyString());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/users/check-duplication")
                .param("userEmail", userEmail))
            .andExpect(status().isConflict());
    }


    @Test
    @DisplayName("올바른 로그인 요청시 200 OK 상태 코드를 반환한다.")
    void loginSuccess() throws Exception {
        // given
        when(userService.findLoggedInUser(any(LoginRequest.class))).thenReturn(user);
        doNothing().when(loginService).login(user.getId());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk());

        // verify
        Mockito.verify(userService).findLoggedInUser(loginRequest);
        Mockito.verify(loginService).login(user.getId());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시도 시 로그인 실패")
    void loginFailureWithEmailNotFound() throws Exception {
        // given
        LoginRequest nonExistentEmailLoginRequest = new LoginRequest("nonexistent@example.com", "password");

        given(userService.findLoggedInUser(any(LoginRequest.class)))
            .willThrow(new NotFoundUserIdException("사용자 아이디가 존재하지 않습니다."));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nonExistentEmailLoginRequest)))
            .andExpect(status().isNotFound());

        // verify
        Mockito.verify(userService, Mockito.times(1)).findLoggedInUser(any(LoginRequest.class));
    }


    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도 시 로그인 실패")
    void loginFailureWithWrongPassword() throws Exception {
        // given
        LoginRequest wrongPasswordLoginRequest = new LoginRequest("test1234@example.com", "wrongPassword");
        given(userService.findLoggedInUser(any(LoginRequest.class)))
            .willThrow(new MisMatchPasswordException("비밀번호가 일치하지 않습니다."));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongPasswordLoginRequest)))
            .andExpect(status().isBadRequest());

        // verify
        Mockito.verify(userService, Mockito.times(1)).findLoggedInUser(any(LoginRequest.class));
    }

    @Test
    @DisplayName("로그아웃 요청시 200 OK 상태 코드를 반환한다.")
    void logoutSuccess() throws Exception {
        // given
        doNothing().when(loginService).logout();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/users/logout")
                .session(mockHttpSession))
            .andExpect(status().isOk());

        // verify
        Mockito.verify(loginService).logout();
    }
}
