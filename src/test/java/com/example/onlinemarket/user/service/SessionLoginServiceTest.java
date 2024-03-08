package com.example.onlinemarket.user.service;

import static com.example.onlinemarket.domain.user.constants.SessionKey.LOGGED_IN_USER;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.service.SessionLoginService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionLoginServiceTest {

    @Mock
    private HttpSession mockHttpSession;
    @InjectMocks
    private SessionLoginService loginService;
    User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1)
            .email("test@example.com")
            .password("test1234")
            .name("Test User")
            .phone("01012345678")
            .build();
        mockHttpSession.setAttribute(LOGGED_IN_USER, null);
    }

    @Test
    @DisplayName("로그인 시 세션에 사용자 ID가 저장되어야 한다")
    void login_ShouldSetUserIdInSession() {
        loginService.login(testUser.getId());

        verify(mockHttpSession).setAttribute(LOGGED_IN_USER, testUser.getId());
    }

    @Test
    @DisplayName("세션 설정 시 예외가 발생하면 에러가 발생한다.")
    void login_ShouldThrowException_When_SessionSettingFails() {
        doThrow(new RuntimeException("세션 설정 실패")).when(mockHttpSession)
            .setAttribute(LOGGED_IN_USER, testUser.getId());

        assertThrows(RuntimeException.class, () -> loginService.login(testUser.getId()));
    }

    @Test
    @DisplayName("로그아웃에 성공한다")
    void logout_Success() {
        mockHttpSession.setAttribute(LOGGED_IN_USER, testUser.getId());

        loginService.logout();

        assertNull(mockHttpSession.getAttribute(LOGGED_IN_USER));
    }
}
