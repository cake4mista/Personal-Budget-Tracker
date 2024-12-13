package com.example.budgettracker.controller;

import com.example.budgettracker.dto.CategoryDto;
import com.example.budgettracker.model.Category;
import com.example.budgettracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        String username = getAuthenticatedUsername();
        List<Category> categories = categoryService.getCategoriesByUser(username);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
        String username = getAuthenticatedUsername();
        Category category = categoryService.createCategory(username, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        String username = getAuthenticatedUsername();
        categoryService.deleteCategory(id, username);
        return ResponseEntity.ok("Category deleted successfully.");
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
