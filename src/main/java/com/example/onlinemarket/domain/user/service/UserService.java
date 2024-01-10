package com.example.onlinemarket.domain.user.service;

import com.example.onlinemarket.common.exception.DuplicatedException;
import com.example.onlinemarket.common.exception.InvalidPasswordException;
import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.common.utils.PasswordEncoder;
import com.example.onlinemarket.domain.user.dto.UserDTO;
import com.example.onlinemarket.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserDTO userDTO) {
        boolean isDuplicatedEmail = isDuplicatedEmail(userDTO.getEmail());
        if (isDuplicatedEmail) {
            throw new DuplicatedException("중복된 이메일입니다.");
        }
        userDTO.setPassword(encryptedPassword(userDTO.getPassword()));

        userMapper.insertUser(userDTO);
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
        return userMapper.existsByEmail(email) == 1;
    }

    private String encryptedPassword(String password) {
        return passwordEncoder.encryptSHA256(password);
    }
}