package com.example.budgettracker.service;

import com.example.budgettracker.dto.CategoryDto;
import com.example.budgettracker.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoriesByUser(String username);
    Category createCategory(String username, CategoryDto categoryDto);
    void deleteCategory(String id, String username);
}
