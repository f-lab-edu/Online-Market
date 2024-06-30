package com.example.onlinemarket.domain.category.controller;

import com.example.onlinemarket.domain.category.dto.CategoryDto;
import com.example.onlinemarket.domain.category.dto.request.CategoryCreateRequest;
import com.example.onlinemarket.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/categories")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Long> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        Long id = categoryService.saveCategory(request);
        return ResponseEntity.created(URI.create("/category/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categoriesDto = categoryService.getCategories();
        return new ResponseEntity<>(categoriesDto, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto categoryId = categoryService.findById(id);
        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategoryName(@PathVariable Long id,
        @RequestBody CategoryDto categoryDto) {
        categoryService.updateCategoryName(id, categoryDto.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
