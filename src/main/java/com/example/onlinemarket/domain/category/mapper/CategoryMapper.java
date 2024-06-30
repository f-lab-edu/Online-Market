package com.example.onlinemarket.domain.category.mapper;

import com.example.onlinemarket.domain.category.entity.Category;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryMapper {

    void insert(Category category);

    boolean existsByName(String name);

    List<Category> findAll();

    Optional<Category> findById(Long id);

    String selectCategoryName(Long cagetoryId);

    void updateName
        (@Param("id") long id,
            @Param("newName") String newName);

    void deleteById(Long id);
}
