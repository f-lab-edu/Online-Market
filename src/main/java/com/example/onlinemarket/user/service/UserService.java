package com.example.onlinemarket.user.service;

import com.example.onlinemarket.exception.DuplicateIdException;
import com.example.onlinemarket.user.dto.UserDTO;
import com.example.onlinemarket.user.mapper.UserMapper;
import com.example.onlinemarket.utils.SHA256Util;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public void register(UserDTO userDTO) {
        boolean duplEmailResult = isDuplicatedEmail(userDTO.getEmail());
        if (duplEmailResult) {
            throw new DuplicateIdException("중복된 이메일입니다.");
        }
        userDTO.setCreatedAt(new Date());
        userDTO.setPassword(SHA256Util.encryptSHA256(userDTO.getPassword()));
        int insertCount = userMapper.register(userDTO);

        if (insertCount != 1) {
            throw new RuntimeException("회원가입 에러 "+ "Params : " + userDTO);
        }
    }
    public boolean isDuplicatedEmail(String email) {
        return userMapper.emailCheck(email) == 1;
    }


}



