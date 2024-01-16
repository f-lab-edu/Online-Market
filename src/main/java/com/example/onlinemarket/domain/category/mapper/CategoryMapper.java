package com.example.onlinemarket.domain.category.mapper;

import com.example.onlinemarket.domain.category.dto.CategoryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

  List<CategoryDTO> findAll();

  CategoryDTO findById(Long id);

  void insert(CategoryDTO category);

  void updateName(CategoryDTO category);

  void deleteById(Long id);

}
