package com.example.onlinemarket.domain.product.mapper;

import com.example.onlinemarket.domain.product.dto.ProductDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    List<ProductDTO> findAll(Long categoryId, int offset, int limit);

    ProductDTO findById(long productId);

    List<ProductDTO> findByNameContaining(@Param("name") String name);

    void insert(ProductDTO productDTO);

    void update(ProductDTO product);

    void deleteById(@Param("id") long productId);

    void updateQuantity(Long id, Long quantity);
}
