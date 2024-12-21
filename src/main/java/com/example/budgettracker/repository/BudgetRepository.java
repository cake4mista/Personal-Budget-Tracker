package com.example.budgettracker.repository;

import com.example.budgettracker.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByUserId(String userId);
}
