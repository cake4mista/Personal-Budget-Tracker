package com.example.budgettracker.repository;

import com.example.budgettracker.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByUsername(String username);
}
