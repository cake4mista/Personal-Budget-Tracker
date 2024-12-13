package com.example.budgettracker.repository;

import com.example.budgettracker.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    Optional<Budget> findByBudgetMonth(YearMonth budgetMonth);
}
