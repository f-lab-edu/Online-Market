package com.example.onlinemarket.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import com.example.onlinemarket.common.utils.PasswordEncryptor;
import com.example.onlinemarket.domain.user.dto.LoginRequest;
import com.example.onlinemarket.domain.user.dto.SignUpRequest;
import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.exception.DuplicatedEmailException;
import com.example.onlinemarket.domain.user.exception.DuplicatedPhoneException;
import com.example.onlinemarket.domain.user.exception.PasswordMisMatchException;
import com.example.onlinemarket.domain.user.exception.UserEmailNotFoundException;
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
            .email("test@example.com")
            .password("testPassword")
            .phone("01012345678")
            .build();

        loginRequest = new LoginRequest("testUserId", "testPassword");

        testUser = User.builder()
            .id(1L)
            .email("test@example.com")
            .password(passwordEncryptor.encrypt("testPassword"))
            .build();
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUpSuccess() {
        willDoNothing().given(userMapper).insertUser(any(User.class));

        userService.signUp(signUpRequest);

        then(userMapper).should().insertUser(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복으로 회원가입에 실패한다.")
    void signUpWithDuplicatedUserEmail() {
        // given
        DuplicateKeyException exception = new DuplicateKeyException(signUpRequest.getEmail());
        willThrow(exception).given(userMapper).insertUser(any(User.class));

        // when & then
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
            .isInstanceOf(DuplicatedEmailException.class)
            .hasMessageContaining("중복된 이메일입니다.");
        then(userMapper).should().insertUser(any(User.class));
    }

    @Test
    @DisplayName("휴대폰 번호 중복으로 회원가입에 실패한다.")
    void signUpWitDuplicatedPhone() {
        // given
        DuplicateKeyException exception = new DuplicateKeyException(signUpRequest.getPhone());
        willThrow(exception).given(userMapper).insertUser(any(User.class));

        // when & then
        assertThatThrownBy(() -> userService.signUp(signUpRequest))
            .isInstanceOf(DuplicatedPhoneException.class)
            .hasMessageContaining("중복된 휴대폰 번호입니다.");
        then(userMapper).should().insertUser(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복체크에 성공한다.")
    void checkUserEmailDuplicationSuccess() {
        given(userMapper.emailExists(signUpRequest.getEmail())).willReturn(0);

        userService.checkUserEmailDuplication(signUpRequest.getEmail());

        then(userMapper).should().emailExists(signUpRequest.getEmail());
    }

    @Test
    @DisplayName("이메일 중복으로 이메일 중복체크에 실패한다.")
    void checkUserEmailDuplicationWithDuplicatedUserEmail() {
        // given
        given(userMapper.emailExists(signUpRequest.getEmail())).willReturn(1);

        // when & then
        assertThatThrownBy(() -> userService.checkUserEmailDuplication(signUpRequest.getEmail()))
            .isInstanceOf(DuplicatedEmailException.class)
            .hasMessageContaining("중복된 이메일입니다.");
        then(userMapper).should().emailExists(signUpRequest.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시도 시 로그인 실패")
    void loginFailureWithNonExistentEmail() {
        // given
        given(userMapper.findByEmail(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.findLoggedInUser(loginRequest))
            .isInstanceOf(UserEmailNotFoundException.class)
            .hasMessageContaining("등록되지 않은 이메일입니다.");
        then(userMapper).should().findByEmail(loginRequest.getEmail());
    }


    @Test
    @DisplayName("비밀번호 오기입으로 로그인에 실패한다.")
    void loginWithInvalidPassword() {
        // given
        given(userMapper.findByEmail(loginRequest.getEmail())).willReturn(Optional.ofNullable(testUser));
        given(passwordEncryptor.isMatch(loginRequest.getPassword(), testUser.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.findLoggedInUser(loginRequest))
            .isInstanceOf(PasswordMisMatchException.class)
            .hasMessageContaining("비밀번호가 일치하지 않습니다.");
        then(userMapper).should().findByEmail(loginRequest.getEmail());
        then(passwordEncryptor).should().isMatch(loginRequest.getPassword(), testUser.getPassword());
    }
}

