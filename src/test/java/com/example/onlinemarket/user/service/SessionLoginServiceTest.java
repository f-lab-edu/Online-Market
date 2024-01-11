package com.example.onlinemarket.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.example.onlinemarket.domain.user.constants.SessionKey;
import com.example.onlinemarket.domain.user.service.SessionLoginService;
import jakarta.servlet.http.HttpSession;
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

    private final int testUserId = 1;

    @Test
    @DisplayName("로그인 시 세션에 사용자 ID가 저장되어야 한다")
    void login_ShouldSetUserIdInSession() {
        loginService.login(testUserId);

        verify(mockHttpSession).setAttribute(SessionKey.LOGGED_IN_USER, testUserId);
    }

    @Test
    @DisplayName("세션 설정 시 예외가 발생하면 에러가 발생해야 한다")
    void login_ShouldThrowException_When_SessionSettingFails() {
        doThrow(new RuntimeException("세션 설정 실패")).when(mockHttpSession)
                .setAttribute(SessionKey.LOGGED_IN_USER, testUserId);

        assertThrows(RuntimeException.class, () -> loginService.login(testUserId));
    }
}
