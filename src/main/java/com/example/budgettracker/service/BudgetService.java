package com.example.budgettracker.service;

import com.example.budgettracker.dto.BudgetDto;
import com.example.budgettracker.model.Budget;

import java.util.List;

public interface BudgetService {
    List<Budget> getBudgetsByUser(String username);
    Budget createBudget(String username, BudgetDto budgetDto);
    void deleteBudget(String id, String username);
}
