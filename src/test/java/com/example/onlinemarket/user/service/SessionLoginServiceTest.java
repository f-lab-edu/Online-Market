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
            .userId("dnzp75")
            .email("testUserEmail")
            .password(passwordEncryptor.encode("testPassword"))
            .build();
    }

    @Test
    @DisplayName("사용자 아이디 로그인 성공 시 세션에 있는 유저 정보 저장한다.")
    void login_ShouldStoreIdInSession() {
        willDoNothing().given(mockSession).setAttribute(SessionKey.LOGGED_IN_USER, testUser.getId());
        given(mockSession.getAttribute(SessionKey.LOGGED_IN_USER)).willReturn(testUser.getId());

        sessionLoginService.login(testUser.getId());

        then(mockSession).should().setAttribute(SessionKey.LOGGED_IN_USER, testUser.getId());
        assertEquals(mockSession.getAttribute(SessionKey.LOGGED_IN_USER), testUser.getId());
    }

    @Test
    @DisplayName("사용자 로그아웃 시 세션에 있는 사용자 정보 제거한다.")
    void logout_ShouldRemoveIdFromSession() {
        willDoNothing().given(mockSession).removeAttribute(SessionKey.LOGGED_IN_USER);
        mockSession.setAttribute(SessionKey.LOGGED_IN_USER, testUser.getId());

        sessionLoginService.logout();

        then(mockSession).should().removeAttribute(SessionKey.LOGGED_IN_USER);
        assertNull(mockSession.getAttribute(SessionKey.LOGGED_IN_USER));
    }


    @Test
    @DisplayName("세션에 저장된 사용자 정보 가져오는데 성공한다.")
    void getLoginUserId_Success() {
        given(mockSession.getAttribute(SessionKey.LOGGED_IN_USER)).willReturn(testUser.getId());

        Optional<Long> result = sessionLoginService.getLoginUserId();

        then(mockSession).should().getAttribute(SessionKey.LOGGED_IN_USER);
        assertEquals(testUser.getId(), result.orElse(null));
    }


    @Test
    @DisplayName("로그인되지 않은 상태에서 사용자 이메일 조회 시 비어있는 Optional 반환")
    void getLoginUserId_WhenNotLoggedIn_ShouldReturnEmptyOptional() {
        given(mockSession.getAttribute(SessionKey.LOGGED_IN_USER)).willReturn(null);

        Optional<Long> result = sessionLoginService.getLoginUserId();

        assertThat(result).isEmpty();
    }

}
