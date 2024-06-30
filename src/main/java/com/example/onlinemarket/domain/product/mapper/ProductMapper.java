package com.example.onlinemarket.domain.product.mapper;

import com.example.onlinemarket.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {

    void insert(Product product);

    List<Product> selectProductsByCategory(Long categoryId, int offset, int limit);

    Optional<Product> findById(long productId);

    List<Product> findByNameContaining(@Param("name") String name);

    void update(Product product);

    void deleteById(@Param("id") long productId);

    void updateQuantity(Long id, Long quantity);
}
