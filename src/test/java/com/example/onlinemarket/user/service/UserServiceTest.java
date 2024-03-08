package com.example.onlinemarket.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.common.exception.DuplicatedEmailException;
import com.example.onlinemarket.common.exception.DuplicatedPhoneException;
import com.example.onlinemarket.common.exception.InvalidPasswordException;
import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.common.utils.PasswordEncoder;
import com.example.onlinemarket.domain.user.dto.LoginRequest;
import com.example.onlinemarket.domain.user.dto.SignUpRequest;
import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.mapper.UserMapper;
import com.example.onlinemarket.domain.user.service.UserService;
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
    private PasswordEncoder passwordEncoder;
    private User testUser;
    private SignUpRequest signUpRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
            .email("test@example.com")
            .password("testPassword")
            .name("Lee GeonHui")
            .phone("01012345678")
            .build();

        testUser = User.builder()
            .id(1L)
            .email("test@example.com")
            .password(passwordEncoder.encryptSHA256("testPassword"))
            .name("name")
            .phone("01012341234")
            .build();

        loginRequest = new LoginRequest("test@example.com", "test1234");
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
    @DisplayName("이메일에 알맞은 패스워드인지 확인한다.")
    void check_Login_Password_Equals_Success() {
        when(userMapper.findByEmail(loginRequest.getEmail())).thenReturn(testUser);
        when(passwordEncoder.encryptSHA256(loginRequest.getPassword())).thenReturn(
            testUser.getPassword());

        User result = userService.findLoggedInUser(loginRequest.getEmail(),
            loginRequest.getPassword());

        assertNotNull(result);
        assertEquals(loginRequest.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("이메일에 따른 패스워드가 아니어서 실패한다.")
    void check_Login_Password_Not_Match_Fails() {
        when(userMapper.findByEmail(loginRequest.getEmail())).thenReturn(testUser);
        when(passwordEncoder.encryptSHA256("wrongPassword")).thenReturn("wrongEncodedPassword");

        assertThrows(InvalidPasswordException.class,
            () -> userService.findLoggedInUser(loginRequest.getEmail(), "wrongPassword"));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인을 시도하면 예외가 발생한다")
    void login_Fail_If_UserDoesNotExist() {
        when(userMapper.findByEmail(loginRequest.getEmail())).thenReturn(null);

        assertThrows(NotFoundException.class,
            () -> userService.findLoggedInUser(loginRequest.getEmail(),
                loginRequest.getPassword()));
    }
}

