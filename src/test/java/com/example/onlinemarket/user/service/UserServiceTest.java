package com.example.onlinemarket.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.common.exception.DuplicatedException;
import com.example.onlinemarket.common.exception.InvalidPasswordException;
import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.common.utils.PasswordEncoder;
import com.example.onlinemarket.domain.user.dto.LoginRequest;
import com.example.onlinemarket.domain.user.dto.UserDTO;
import com.example.onlinemarket.domain.user.mapper.UserMapper;
import com.example.onlinemarket.domain.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserDTO testUser;
    private UserDTO userdto;
    SignUpRequest signUpRequest;
    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        String encryptedPassword = "encryptedPassword";
        testUser = new UserDTO("test@example.com", encryptedPassword, "Test User", "01012341234");
        userdto = new UserDTO("email@naver.com", "password", "name", "01012345678");
        loginRequest = new LoginRequest("test@example.com", "test1234");
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUp_Success() {
        when(userMapper.existsByEmail("email@naver.com")).thenReturn(0);
        when(passwordEncoder.encryptSHA256("password")).thenReturn("encodedPassword");

        userService.signUp(userdto);

        verify(userMapper).insertUser(any(UserDTO.class));
    }

    @Test
    @DisplayName("해당 유저의 email이 존재하는지 확인한다.")
    void exists_By_Email_True() {
        when(userMapper.existsByEmail(userdto.getEmail())).thenReturn(1);

        boolean exists = userService.isDuplicatedEmail(userdto.getEmail());
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("해당 유저의 email이 존재하지 않는다.")
    void exists_By_Email_False() {
        when(userMapper.existsByEmail(userdto.getEmail())).thenReturn(0);

        boolean exists = userService.isDuplicatedEmail(userdto.getEmail());
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("이메일 중복으로 회원가입에 실패한다")
    void signUp_Fail_DuplicateEmail() {
        when(userMapper.existsByEmail(userdto.getEmail())).thenReturn(1);

        assertThrows(DuplicatedException.class, () -> userService.signUp(userdto));
    }

    @Test
    @DisplayName("이메일에 알맞은 패스워드가 올바른지 확인한다.")
    void check_Login_Password_Equals_Success() {
        when(userMapper.findByEmail(loginRequest.getEmail())).thenReturn(testUser);

        String encryptedPassword = "encryptedPassword";
        when(passwordEncoder.encryptSHA256(loginRequest.getPassword())).thenReturn(
                encryptedPassword);

        UserDTO result = userService.findLoggedInUser(loginRequest.getEmail(),
                loginRequest.getPassword());

        assertNotNull(result);
        assertEquals(loginRequest.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("이메일에 따른 패스워드가 아니어서 실패한다.")
    void check_Login_Password_Not_Match_Fails() {
        String wrongPassword = "wrongPassword";
        when(userMapper.findByEmail(loginRequest.getEmail())).thenReturn(testUser);

        when(passwordEncoder.encryptSHA256(wrongPassword)).thenReturn("wrongEncodedPassword");

        assertThrows(InvalidPasswordException.class,
                () -> userService.findLoggedInUser(loginRequest.getEmail(), wrongPassword));
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
