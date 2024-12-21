package com.example.budgettracker.service;

import com.example.budgettracker.dto.BudgetDto;
import com.example.budgettracker.model.Budget;

import java.util.List;

public interface BudgetService {
    Budget createBudget(String userId, BudgetDto budgetDto);
    List<Budget> getBudgetsByUserId(String userId);
    List<Budget> getAllBudgets();
    void deleteBudget(String userId, String budgetId); // New method
}

