package com.example.onlinemarket.domain.user.service;

import com.example.onlinemarket.common.utils.PasswordEncryptor;
import com.example.onlinemarket.domain.user.dto.UserDto;
import com.example.onlinemarket.domain.user.dto.request.LoginRequest;
import com.example.onlinemarket.domain.user.dto.request.SignUpRequest;
import com.example.onlinemarket.domain.user.entity.User;
import com.example.onlinemarket.domain.user.exception.DuplicatedPhoneException;
import com.example.onlinemarket.domain.user.exception.DuplicatedUserIdException;
import com.example.onlinemarket.domain.user.exception.MisMatchPasswordException;
import com.example.onlinemarket.domain.user.exception.NotFoundUserIdException;
import com.example.onlinemarket.domain.user.mapper.UserMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncryptor passwordEncryptor;
    private Logger log;

    public void signUp(SignUpRequest request) {
        String password = passwordEncryptor.encode(request.getPassword());
        User user = request.toEntity(password);

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            if (e.getMessage().contains(user.getUserId())) {
                throw new DuplicatedUserIdException("이미 존재하는 아이디입니다.");
            }
            if (e.getMessage().contains(user.getPhone())) {
                throw new DuplicatedPhoneException("이미 존재하는 휴대폰 번호입니다.");
            }
        }
    }

    public void checkUserIdDuplication(String userId) {
        if (userMapper.existsByUserId(userId)) {
            throw new DuplicatedUserIdException("이미 존재하는 아이디입니다.");
        }
    }

    public UserDto findLoggedInUser(LoginRequest request) {
        User loginUser = userMapper.findByUserId(request.getUserId())
            .orElseThrow(() -> new NotFoundUserIdException("아이디를 찾을 수 없습니다."));

        boolean isValidPassword = passwordEncryptor.isMatch(request.getPassword(), loginUser.getPassword());
        if (!isValidPassword) {
            throw new MisMatchPasswordException("비밀번호가 일치하지 않습니다.");
        }

        return UserDto.of(loginUser);
    }

    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }

    public boolean isExists(Long userId) {
        return userMapper.existsById(userId);
    }
}
