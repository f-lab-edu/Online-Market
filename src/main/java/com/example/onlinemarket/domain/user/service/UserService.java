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

    public void signUp(SignUpRequest request) {
        boolean isDuplicatedEmail = isDuplicatedEmail(request.getEmail());
        if (isDuplicatedEmail) {
            throw new DuplicatedException("중복된 이메일입니다.");
        }
        String encryptedPassword = encryptedPassword(request.getPassword());
        UserDTO user = request.toEntity(encryptedPassword);

        userMapper.insertUser(user);
    }

    public UserDTO findLoggedInUser(String email, String password) {
        UserDTO user = userMapper.findByEmail(email);

        if (user == null) {
            throw new NotFoundException();
        }

        if (!encryptedPassword(password).equals(user.getPassword())) {
            throw new InvalidPasswordException();
        }
        return user;
    }

    public boolean isDuplicatedEmail(String email) {
        return userMapper.emailExists(email) == 1;
    }

    private String encryptedPassword(String password) {
        return passwordEncoder.encryptSHA256(password);
    }
}
