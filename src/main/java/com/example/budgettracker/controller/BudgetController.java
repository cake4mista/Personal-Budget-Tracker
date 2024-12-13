package com.example.budgettracker.controller;

import com.example.budgettracker.model.Budget;
import com.example.budgettracker.service.BudgetService;
import com.example.budgettracker.dto.BudgetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<Budget> setBudget(@RequestBody BudgetDto budgetDto) {
        Budget budget = new Budget(budgetDto.getMonthlyLimit(), YearMonth.parse(budgetDto.getBudgetMonth()));
        return ResponseEntity.ok(budgetService.setMonthlyBudget(budget));
    }

    @GetMapping
    public ResponseEntity<Budget> getBudgetByMonth(@RequestParam String month) {
        Optional<Budget> budget = budgetService.getBudgetByMonth(YearMonth.parse(month));
        return budget.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
