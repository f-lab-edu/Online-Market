package com.example.onlinemarket.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.onlinemarket.exception.DuplicateIdException;
import com.example.onlinemarket.user.dto.UserDTO;
import com.example.onlinemarket.user.mapper.UserMapper;
import com.example.onlinemarket.utils.SHA256Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUp_Success() {

        // given
        final UserDTO userDTO = new UserDTO("email", "password", "name","phone");

        given(userMapper.emailCheck("email")).willReturn(0);
        given(SHA256Util.encryptSHA256(userDTO.getPassword())).willReturn("encodedPassword");

        // when
        userService.register(userDTO);

        // then
        verify(userMapper).register(any(UserDTO.class));
    }

    @Test
    @DisplayName("회원 이메일이 중복되면 회원가입을 실패한다.")
    void signUp_Fail_Duplicate_Email() {

        // given
        final UserDTO userDTO = new UserDTO("email", "password", "name","phone");

        given(userMapper.emailCheck("email")).willReturn(1);

        // when, then
        assertThrows(DuplicateIdException.class, () -> userService.register(userDTO));
    }


}
