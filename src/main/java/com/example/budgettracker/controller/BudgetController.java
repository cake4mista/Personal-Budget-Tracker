package com.example.budgettracker.controller;

import com.example.budgettracker.dto.BudgetDto;
import com.example.budgettracker.model.Budget;
import com.example.budgettracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets() {
        String username = getAuthenticatedUsername();
        List<Budget> budgets = budgetService.getBudgetsByUser(username);
        return ResponseEntity.ok(budgets);
    }

    @PostMapping
    public ResponseEntity<?> createBudget(@RequestBody BudgetDto budgetDto) {
        System.out.println("DEBUG: Received Payload - Month: " + budgetDto.getMonth() + ", Amount: " + budgetDto.getAmount());

        if (budgetDto.getMonth() == null || budgetDto.getMonth().trim().isEmpty() || budgetDto.getAmount() <= 0) {
            System.out.println("ERROR: Invalid budget data.");
            return ResponseEntity.badRequest().body("Invalid budget data.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("DEBUG: Authenticated Username: " + username);

        Budget createdBudget = budgetService.createBudget(username, budgetDto);
        System.out.println("DEBUG: Saved Budget ID: " + createdBudget.getId());
        return ResponseEntity.ok(createdBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable String id) {
        String username = getAuthenticatedUsername();
        budgetService.deleteBudget(id, username);
        return ResponseEntity.ok("Budget deleted successfully.");
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}