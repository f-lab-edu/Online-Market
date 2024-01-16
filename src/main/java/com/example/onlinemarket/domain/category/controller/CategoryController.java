package com.example.onlinemarket.domain.category.controller;

import com.example.onlinemarket.domain.category.dto.CategoryDTO;
import com.example.onlinemarket.domain.category.service.CategoryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<CategoryDTO>> getAllCategories() {

    List<CategoryDTO> categories = categoryService.getCategories();

    return ResponseEntity.ok().body(categories);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {

    CategoryDTO category = categoryService.findById(id);

    return ResponseEntity.ok().body(category);
  }

  @PostMapping
  public ResponseEntity<Long> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {

    Long id = categoryService.saveCategory(categoryDTO);

    return ResponseEntity.created(URI.create("/category/" + id)).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateCategoryName(@PathVariable Long id,
      @RequestBody CategoryDTO categoryDTO) {

    categoryService.updateCategoryName(id, categoryDTO);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

    categoryService.deleteCategory(id);

    return ResponseEntity.noContent().build();
  }
}
