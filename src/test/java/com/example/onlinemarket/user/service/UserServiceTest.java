package com.example.onlinemarket.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.onlinemarket.common.exception.DuplicatedException;
import com.example.onlinemarket.common.utils.PasswordEncoder;
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
    UserDTO userdto;

    @BeforeEach
    void setUp() {
        userdto = new UserDTO("email@naver.com", "password", "name", "01012345678");
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
}