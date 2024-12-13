package com.example.budgettracker.service.impl;

import com.example.budgettracker.dto.BudgetDto;
import com.example.budgettracker.model.Budget;
import com.example.budgettracker.repository.BudgetRepository;
import com.example.budgettracker.service.BudgetService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public List<Budget> getBudgetsByUser(String username) {
        return budgetRepository.findByUsername(username);
    }

    @Override
    public Budget createBudget(String username, BudgetDto budgetDto) {
        System.out.println("DEBUG: Saving budget for user: " + username);

        Budget budget = new Budget();
        budget.setUsername(username);
        budget.setMonth(budgetDto.getMonth());
        budget.setAmount(budgetDto.getAmount());

        System.out.println("DEBUG: Budget to Save - Username: " + budget.getUsername()
                + ", Month: " + budget.getMonth()
                + ", Amount: " + budget.getAmount());

        try {
            Budget savedBudget = budgetRepository.save(budget);

            if (savedBudget != null && savedBudget.getId() != null) {
                System.out.println("DEBUG: Successfully Saved Budget ID: " + savedBudget.getId());
            } else {
                System.out.println("ERROR: Budget not saved successfully!");
            }

            return savedBudget;

        } catch (Exception e) {
            System.out.println("ERROR: Exception while saving budget - " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to ensure you see errors in logs
        }
    }

    @Override
    public void deleteBudget(String id, String username) {
        Optional<Budget> budget = budgetRepository.findById(id);
        if (budget.isPresent() && budget.get().getUsername().equals(username)) {
            budgetRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("Unauthorized to delete this budget.");
        }
    }
}
