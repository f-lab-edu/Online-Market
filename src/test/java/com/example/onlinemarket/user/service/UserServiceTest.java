package com.example.onlinemarket.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.example.onlinemarket.common.utils.PasswordEncryptor;
import com.example.onlinemarket.domain.user.dto.request.LoginRequest;
import com.example.onlinemarket.domain.user.dto.request.SignUpRequest;
import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.exception.DuplicatedPhoneException;
import com.example.onlinemarket.domain.user.exception.DuplicatedUserIdException;
import com.example.onlinemarket.domain.user.exception.MisMatchPasswordException;
import com.example.onlinemarket.domain.user.exception.NotFoundUserIdException;
import com.example.onlinemarket.domain.user.mapper.UserMapper;
import com.example.onlinemarket.domain.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncryptor passwordEncryptor;
    private User testUser;
    private SignUpRequest signUpRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
            .userId(("dnzp75"))
            .password("testPassword")
            .email("test@example.com")
            .name("GeonHui")
            .phone("01012345678")
            .build();

        loginRequest = new LoginRequest("testUserId", "testPassword");

        testUser = User.builder()
            .id(1L)
            .userId("dnzp75")
            .email("test@example.com")
            .password(passwordEncryptor.encode("testPassword"))
            .build();
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUpSuccess() {
        willDoNothing().given(userMapper).insert(any(User.class));

        userService.signUp(signUpRequest);

        then(userMapper).should().insert(any(User.class));
    }

    @Test
    @DisplayName("아이디 중복으로 회원가입에 실패한다.")
    void signUpWithDuplicatedUserEmail() {
        // given
        DuplicateKeyException exception = new DuplicateKeyException(signUpRequest.getUserId());
        willThrow(exception).given(userMapper).insert(any(User.class));

        // when & then
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
            .isInstanceOf(DuplicatedUserIdException.class)
            .hasMessageContaining("중복된 아이디입니다.");
        then(userMapper).should().insert(any(User.class));
    }

    // 이메일 중복 테스트 필요

    @Test
    @DisplayName("휴대폰 번호 중복으로 회원가입에 실패한다.")
    void signUpWitDuplicatedPhone() {
        // given
        DuplicateKeyException exception = new DuplicateKeyException(signUpRequest.getPhone());
        willThrow(exception).given(userMapper).insert(any(User.class));

        // when & then
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
            .isInstanceOf(DuplicatedPhoneException.class)
            .hasMessageContaining("중복된 휴대폰 번호입니다.");
        then(userMapper).should().insert(any(User.class));
    }

    @Test
    @DisplayName("아이디 중복체크에 성공한다.")
    void checkUserIdDuplicationSuccess() {
        given(userMapper.existsByUserId(signUpRequest.getUserId())).willReturn(true);

        userService.checkUserIdDuplication(signUpRequest.getUserId());

        then(userMapper).should().existsByUserId(signUpRequest.getUserId());
    }

    @Test
    @DisplayName("아이디 중복으로 아이디 중복체크에 실패한다.")
    void checkUserIdDuplicationWithDuplicatedUserId() {
        // given
        given(userMapper.existsByUserId(signUpRequest.getEmail())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.checkUserIdDuplication(signUpRequest.getUserId()))
            .isInstanceOf(DuplicatedUserIdException.class)
            .hasMessageContaining("중복된 아이디입니다.");
        then(userMapper).should().existsByUserId(signUpRequest.getUserId());
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 시도 시 로그인 실패")
    void loginFailureWithNonExistentEmail() {
        // given
        given(userMapper.findByUserId(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.findLoggedInUser(loginRequest))
            .isInstanceOf(NotFoundUserIdException.class)
            .hasMessageContaining("등록되지 않은 아이디입니다.");
        then(userMapper).should().findByUserId(loginRequest.getUserId());
    }


    @Test
    @DisplayName("잘못된 비밀번호로 로그인에 실패한다.")
    void loginWithInvalidPassword() {
        // given
        given(userMapper.findByUserId(loginRequest.getUserId())).willReturn(Optional.ofNullable(testUser));
        given(passwordEncryptor.isMatch(loginRequest.getPassword(), testUser.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.findLoggedInUser(loginRequest))
            .isInstanceOf(MisMatchPasswordException.class)
            .hasMessageContaining("비밀번호가 일치하지 않습니다.");
        then(userMapper).should().findByUserId(loginRequest.getUserId());
        then(passwordEncryptor).should().isMatch(loginRequest.getPassword(), testUser.getPassword());
    }
}

