package com.example.onlinemarket.user.mapper;

import com.example.onlinemarket.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    int register(UserDTO userDTO);

    int emailCheck(String email);


}
