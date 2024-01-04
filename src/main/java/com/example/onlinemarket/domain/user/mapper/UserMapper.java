package com.example.onlinemarket.domain.user.mapper;

import com.example.onlinemarket.domain.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int insertUser(UserDTO userDTO);

    int existsByEmail(String email);

    UserDTO findByEmail(String email);

}