package com.example.budgettracker.service.impl;

import com.example.budgettracker.dto.ExpenseDto;
import com.example.budgettracker.model.Expense;
import com.example.budgettracker.repository.ExpenseRepository;
import com.example.budgettracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public Expense createExpense(String userId, ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setDate(expenseDto.getDate());
        expense.setCategory(expenseDto.getCategory());
        expense.setDescription(expenseDto.getDescription());
        expense.setAmount(expenseDto.getAmount());
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesByUserId(String userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public void deleteExpense(String userId, String id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + id));
        if (!expense.getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own expenses.");
        }
        expenseRepository.deleteById(id);
    }
}
