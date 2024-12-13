package com.example.budgettracker.service.impl;

import com.example.budgettracker.dto.ExpenseDto;
import com.example.budgettracker.model.Expense;
import com.example.budgettracker.repository.ExpenseRepository;
import com.example.budgettracker.service.ExpenseService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<Expense> getExpensesByUser(String username) {
        return expenseRepository.findByUsername(username);
    }

    @Override
    public Expense createExpense(String username, ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setUsername(username);
        expense.setDescription(expenseDto.getDescription());
        expense.setAmount(expenseDto.getAmount());
        expense.setDate(expenseDto.getDate());
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(String id, String username) {
        Optional<Expense> expense = expenseRepository.findById(id);
        if (expense.isPresent() && expense.get().getUsername().equals(username)) {
            expenseRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("Unauthorized to delete this expense.");
        }
    }
}
