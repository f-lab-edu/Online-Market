package com.example.onlinemarket.domain.user.mapper;

import com.example.onlinemarket.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    int emailExists(String userEmail);

    User findByEmail(String email);
}
