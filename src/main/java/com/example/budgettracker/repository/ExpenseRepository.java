package com.example.budgettracker.repository;

import com.example.budgettracker.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUsername(String username);
}
