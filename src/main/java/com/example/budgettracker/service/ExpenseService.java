package com.example.budgettracker.service;

import com.example.budgettracker.dto.ExpenseDto;
import com.example.budgettracker.model.Expense;

import java.util.List;

public interface ExpenseService {
    Expense createExpense(String userId, ExpenseDto expenseDto);
    List<Expense> getExpensesByUserId(String userId);
    void deleteExpense(String userId, String id);
}
