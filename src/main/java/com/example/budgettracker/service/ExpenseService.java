package com.example.budgettracker.service;

import com.example.budgettracker.dto.ExpenseDto;
import com.example.budgettracker.model.Expense;

import java.util.List;

public interface ExpenseService {
    List<Expense> getExpensesByUser(String username);
    Expense createExpense(String username, ExpenseDto expenseDto);
    void deleteExpense(String id, String username);
}
