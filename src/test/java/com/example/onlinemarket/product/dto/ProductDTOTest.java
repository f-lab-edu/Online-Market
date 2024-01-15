package com.example.onlinemarket.product.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.onlinemarket.domain.product.dto.ProductDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductDTOTest {

    private final Validator validator;

    public ProductDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 ProductDTO 생성")
    public void whenValidProduct_thenNoConstraintViolations() {
        ProductDTO product = new ProductDTO(1L, "Valid Product", 1000.0, 40, "Valid Description");

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("유효하지 않은 이름 - Null")
    public void whenNullName_thenOneConstraintViolation() {
        ProductDTO product = new ProductDTO(1, null, 1000.0, 40, "Valid Description");

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("유효하지 않은 가격 - 음수")
    public void whenNegativePrice_thenConstraintViolation() {
        ProductDTO product = new ProductDTO(1, "Product", -10.0, 40, "Valid Description");

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("유효하지 않은 설명 - 너무 짧음")
    public void whenShortDescription_thenConstraintViolation() {
        ProductDTO product = new ProductDTO(1, "Product", 1000.0, 40, "Short");

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("유효하지 않은 설명 - 너무 길음")
    public void whenLongDescription_thenConstraintViolation() {
        String longDescription = "This is a very long description".repeat(10);
        ProductDTO product = new ProductDTO(1, "Product", 1000.0, 40, longDescription);

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

        assertFalse(violations.isEmpty());
    }
}
