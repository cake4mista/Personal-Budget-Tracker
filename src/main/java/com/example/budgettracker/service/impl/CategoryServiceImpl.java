package com.example.budgettracker.service.impl;

import com.example.budgettracker.dto.CategoryDto;
import com.example.budgettracker.model.Category;
import com.example.budgettracker.repository.CategoryRepository;
import com.example.budgettracker.service.CategoryService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getCategoriesByUser(String username) {
        return categoryRepository.findByUsername(username);
    }

    @Override
    public Category createCategory(String username, CategoryDto categoryDto) {
        Category category = new Category();
        category.setUsername(username);
        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id, String username) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent() && category.get().getUsername().equals(username)) {
            categoryRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("Unauthorized to delete this category.");
        }
    }
}
