package com.example.budgettracker.service;

import com.example.budgettracker.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense);
    List<Expense> getAllExpenses();
    List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate);
    List<Expense> getExpensesByCategoryId(String categoryId);
    void deleteExpense(String expenseId);
}
