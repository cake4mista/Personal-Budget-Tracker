package com.example.budgettracker.controller;

import com.example.budgettracker.model.Expense;
import com.example.budgettracker.service.ExpenseService;
import com.example.budgettracker.dto.ExpenseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@RequestBody ExpenseDto expenseDto) {
        Expense expense = new Expense(
                expenseDto.getDescription(),
                expenseDto.getAmount(),
                LocalDate.parse(expenseDto.getExpenseDate()),
                null // The category must be fetched and set here if required
        );
        return ResponseEntity.ok(expenseService.addExpense(expense));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(
                expenseService.getExpensesByDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted successfully.");
    }
}
