package com.example.onlinemarket.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.example.onlinemarket.common.utils.PasswordEncryptor;
import com.example.onlinemarket.domain.user.constants.SessionKey;
import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.service.SessionLoginService;
import java.util.Optional;
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

    @Mock
    private MockHttpSession mockSession;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @InjectMocks
    private SessionLoginService sessionLoginService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .email("testUserEmail")
            .password(passwordEncryptor.encrypt("testPassword"))
            .build();
    }

    @Test
    @DisplayName("사용자 이메일로 로그인 성공 시 세션에 이메일 저장")
    void login_ShouldStoreEmailInSession() {
        willDoNothing().given(mockSession).setAttribute(SessionKey.LOGGED_IN_USER, testUser.getEmail());
        given(mockSession.getAttribute(SessionKey.LOGGED_IN_USER)).willReturn(testUser.getEmail());

        sessionLoginService.login(testUser.getEmail());

        then(mockSession).should().setAttribute(SessionKey.LOGGED_IN_USER, testUser.getEmail());
        assertEquals(mockSession.getAttribute(SessionKey.LOGGED_IN_USER), testUser.getEmail());
    }

    @Test
    @DisplayName("사용자 로그아웃 시 세션에서 이메일 제거")
    void logout_ShouldRemoveEmailFromSession() {
        willDoNothing().given(mockSession).removeAttribute(SessionKey.LOGGED_IN_USER);
        mockSession.setAttribute(SessionKey.LOGGED_IN_USER, testUser.getEmail());

        sessionLoginService.logout();

        then(mockSession).should().removeAttribute(SessionKey.LOGGED_IN_USER);
        assertNull(mockSession.getAttribute(SessionKey.LOGGED_IN_USER));
    }


    @Test
    @DisplayName("세션에 저장된 사용자의 이메일을 가져오는데 성공한다.")
    void getLoginUserEmail_Success() {
        given(mockSession.getAttribute(SessionKey.LOGGED_IN_USER)).willReturn(testUser.getEmail());

        Optional<String> result = sessionLoginService.getLoginUserEmail();

        then(mockSession).should().getAttribute(SessionKey.LOGGED_IN_USER);
        assertEquals(testUser.getEmail(), result.orElse(""));
    }


    @Test
    @DisplayName("로그인되지 않은 상태에서 사용자 이메일 조회 시 비어있는 Optional 반환")
    void getLoginUserEmail_WhenNotLoggedIn_ShouldReturnEmptyOptional() {
        given(mockSession.getAttribute(SessionKey.LOGGED_IN_USER)).willReturn(null);

        Optional<String> result = sessionLoginService.getLoginUserEmail();

        assertThat(result).isEmpty();
    }

}
