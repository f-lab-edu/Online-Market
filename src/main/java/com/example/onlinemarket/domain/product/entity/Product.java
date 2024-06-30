package com.example.onlinemarket.domain.product.entity;

import com.example.onlinemarket.domain.order.exception.OrderQuantityExceedsStockException;
import com.example.onlinemarket.domain.product.exception.NoProductQuantityException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class Product {

    private Long id;
    private Long categoryId;
    private String name;
    private Long price;
    private Long quantity;
    private String description;
    private boolean isOutOfStock;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Product(Long id, Long categoryId, String name, Long price, Long quantity, String description, boolean isOutOfStock, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.isOutOfStock = this.quantity == 0;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public void decreaseStock(Long quantity) {
        validateStock(quantity);
        this.quantity -= quantity;
        this.isOutOfStock = this.quantity == 0; // 수량이 0이면 품절 상태로 설정
    }

    private void validateStock(Long quantity) {
        if (this.quantity == 0) {
            throw new NoProductQuantityException("상품 수량이 소진 되었습니다.");
        }

        if (this.quantity < quantity) {
            throw new OrderQuantityExceedsStockException("최대 상품 수량을 초과 하였습니다.");
        }
    }
}
