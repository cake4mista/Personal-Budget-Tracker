package com.example.budgettracker.service.impl;

import com.example.budgettracker.model.Expense;
import com.example.budgettracker.repository.ExpenseRepository;
import com.example.budgettracker.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByExpenseDateBetween(startDate, endDate);
    }

    @Override
    public List<Expense> getExpensesByCategoryId(String categoryId) {
        return expenseRepository.findByCategoryId(categoryId);
    }

    @Override
    public void deleteExpense(String expenseId) {
        expenseRepository.deleteById(expenseId);
    }
}
