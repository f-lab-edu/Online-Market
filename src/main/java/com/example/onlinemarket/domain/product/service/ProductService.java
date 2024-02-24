package com.example.onlinemarket.domain.product.service;

import com.example.onlinemarket.common.Redis.RedisService;
import com.example.onlinemarket.common.exception.NotFoundException;
import com.example.onlinemarket.domain.product.dto.ProductDTO;
import com.example.onlinemarket.domain.product.mapper.ProductMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final RedisService redisService;

    public List<ProductDTO> getAllProducts(Long categoryId, int page, int limit) {
        String key = "category:" + categoryId + ":page:" + page + ":limit:" + limit;

        List<ProductDTO> products = (List<ProductDTO>) redisService.getValues(key);
        if (products == null) {
            int offset = (page - 1) * limit;
            products = productMapper.findAll(categoryId, offset, limit);
            redisService.setValues(key, products, 10, TimeUnit.MINUTES);
        }

        return products;
    }

    public List<ProductDTO> searchProductsByName(String name) {
        List<ProductDTO> searchedProducts = productMapper.findByNameContaining(name);

        if (searchedProducts.isEmpty()) {
            throw new NotFoundException("상품 이름에 따른 검색 결과가 없습니다.");
        }

        return searchedProducts;
    }


    public ProductDTO getProductById(long id) {
        ProductDTO product = productMapper.findById(id);

        if (product == null) {
            throw new NotFoundException("Products not found");
        }
        return product;
    }


    public long createProduct(ProductDTO productDTO) {
        productMapper.insert(productDTO);

        return productDTO.getId();
    }


    public ProductDTO updateProduct(ProductDTO productDTO) {
        ProductDTO existingProduct = getProductById(productDTO.getId());

        if (existingProduct == null) {
            throw new NotFoundException("상품 id가 존재하지 않습니다.");
        }
        productMapper.update(productDTO);

        return existingProduct;
    }


    public void deleteProduct(int id) {
        getProductById(id);

        productMapper.deleteById(id);
    }
}
