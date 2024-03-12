package com.example.onlinemarket.domain.user.service;

import com.example.onlinemarket.common.utils.PasswordEncryptor;
import com.example.onlinemarket.domain.user.dto.LoginRequest;
import com.example.onlinemarket.domain.user.dto.SignUpRequest;
import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.exception.DuplicatedEmailException;
import com.example.onlinemarket.domain.user.exception.DuplicatedPhoneException;
import com.example.onlinemarket.domain.user.exception.PasswordMisMatchException;
import com.example.onlinemarket.domain.user.exception.UserEmailNotFoundException;
import com.example.onlinemarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncryptor passwordEncryptor;

    public void signUp(SignUpRequest signUpRequest) {
        String password = passwordEncryptor.encrypt(signUpRequest.getPassword());
        User newUser = signUpRequest.toEntity(password);

        try {
            userMapper.insertUser(newUser);
        } catch (DuplicateKeyException e) {
            if (e.getMessage().contains(newUser.getEmail())) {
                throw new DuplicatedEmailException("중복된 이메일입니다.");
            }
            if (e.getMessage().contains(newUser.getPhone())) {
                throw new DuplicatedPhoneException("중복된 휴대폰 번호입니다.");
            }
        }
    }

    public void checkUserEmailDuplication(String userEmail) {
        if (userMapper.emailExists(userEmail) == 1) {
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
    }

    public User findLoggedInUser(LoginRequest loginRequest) {
        User loginUser = userMapper.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UserEmailNotFoundException("등록되지 않은 이메일입니다."));

        boolean isValidPassword = passwordEncryptor.isMatch(loginRequest.getPassword(), loginUser.getPassword());
        if (!isValidPassword) {
            throw new PasswordMisMatchException("비밀번호가 일치하지 않습니다.");
        }

        return loginUser;
    }
}
