package com.example.onlinemarket.domain.user.service;

import com.example.onlinemarket.common.exception.DuplicatedException;
import com.example.onlinemarket.common.exception.InvalidPasswordException;
import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.common.utils.PasswordEncoder;
import com.example.onlinemarket.domain.user.dto.SignUpRequest;
import com.example.onlinemarket.domain.user.dto.UserDTO;
import com.example.onlinemarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest signUpRequest) {
        String password = encryptedPassword(signUpRequest.getPassword());
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

    public User findLoggedInUser(String email, String password) {
        User user = userMapper.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (!encryptedPassword(password).equals(user.getPassword())) {
            throw new InvalidPasswordException();
        }
        return user;
    }

    private String encryptedPassword(String password) {
        return passwordEncoder.encryptSHA256(password);
    }
}
