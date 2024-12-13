package com.example.budgettracker.controller;

import com.example.budgettracker.dto.ExpenseDto;
import com.example.budgettracker.model.Expense;
import com.example.budgettracker.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses() {
        String username = getAuthenticatedUsername();
        List<Expense> expenses = expenseService.getExpensesByUser(username);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody ExpenseDto expenseDto) {
        String username = getAuthenticatedUsername();
        Expense expense = expenseService.createExpense(username, expenseDto);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable String id) {
        String username = getAuthenticatedUsername();
        expenseService.deleteExpense(id, username);
        return ResponseEntity.ok("Expense deleted successfully.");
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
