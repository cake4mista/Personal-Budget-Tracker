package com.example.budgettracker.service.impl;

import com.example.budgettracker.dto.BudgetDto;
import com.example.budgettracker.model.Budget;
import com.example.budgettracker.repository.BudgetRepository;
import com.example.budgettracker.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Budget createBudget(String userId, BudgetDto budgetDto) {
        Budget budget = new Budget();
        budget.setUserId(userId);
        budget.setDate(budgetDto.getDate());
        budget.setDescription("Budget for " + budgetDto.getDate());
        budget.setAmount(budgetDto.getAmount());
        return budgetRepository.save(budget);
    }

    @Override
    public List<Budget> getBudgetsByUserId(String userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Override
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public void deleteBudget(String userId, String budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found with ID: " + budgetId));

        if (!budget.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this budget.");
        }

        budgetRepository.delete(budget);
    }

}
