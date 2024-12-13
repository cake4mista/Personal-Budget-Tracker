package com.example.budgettracker.service.impl;

import com.example.budgettracker.model.Budget;
import com.example.budgettracker.repository.BudgetRepository;
import com.example.budgettracker.service.BudgetService;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Budget setMonthlyBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Optional<Budget> getBudgetByMonth(YearMonth budgetMonth) {
        return budgetRepository.findByBudgetMonth(budgetMonth);
    }
}
