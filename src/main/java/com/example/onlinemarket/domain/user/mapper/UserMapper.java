package com.example.onlinemarket.domain.user.mapper;

import com.example.onlinemarket.domain.user.entity.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void insert(User user);

    boolean existsByUserId(String userId);

    boolean existsById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String userId);

    Optional<User> findById(Long id);
}
