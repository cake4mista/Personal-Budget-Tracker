package com.example.budgettracker.service;

import com.example.budgettracker.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getAllCategories();
    Optional<Category> getCategoryByName(String name);
}
