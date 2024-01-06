package com.example.onlinemarket.user.service;

import static com.example.onlinemarket.domain.user.constants.SessionKey.LOGIN_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.common.utils.PasswordEncoder;
import com.example.onlinemarket.domain.user.dto.LoginRequest;
import com.example.onlinemarket.domain.user.dto.UserDTO;
import com.example.onlinemarket.domain.user.service.SessionLoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

@ExtendWith(MockitoExtension.class)
class SessionLoginServiceTest {

    @InjectMocks
    SessionLoginService loginService;
    @Mock
    MockHttpSession mockHttpSession;
    @Mock
    PasswordEncoder passwordEncoder;
    UserDTO testUser;
    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = UserDTO.builder()
                .id(1)
                .email("test@example.com")
                .password(passwordEncoder.encryptSHA256("test1234"))
                .build();
        loginRequest = new LoginRequest("test@example.com", "test1234");
        loginService = new SessionLoginService(mockHttpSession);
    }

    @Test
    @DisplayName("세션에 사용자ID 확인되면 로그인을 성공한다.")
    void login_Success() {
        when(mockHttpSession.getAttribute(LOGIN_USER)).thenReturn(testUser.getId());

        loginService.login(testUser.getId());

        assertEquals(testUser.getId(), mockHttpSession.getAttribute(LOGIN_USER));
    }
}