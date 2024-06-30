package com.example.onlinemarket.domain.product.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductListResponse {

    private List<ProductResponse> productResponseList;
}
