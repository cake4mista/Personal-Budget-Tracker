package com.example.budgettracker.service;

import com.example.budgettracker.model.Budget;

import java.time.YearMonth;
import java.util.Optional;

public interface BudgetService {
    Budget setMonthlyBudget(Budget budget);
    Optional<Budget> getBudgetByMonth(YearMonth budgetMonth);
}
