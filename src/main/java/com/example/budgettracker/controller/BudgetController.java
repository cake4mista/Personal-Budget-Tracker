package com.example.budgettracker.controller;

import com.example.budgettracker.dto.BudgetDto;
import com.example.budgettracker.model.Budget;
import com.example.budgettracker.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/create")
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetDto budgetDto,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername(); // JWT-derived userId
        return ResponseEntity.ok(budgetService.createBudget(userId, budgetDto));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Budget>> getBudgetsByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(budgetService.getBudgetsByUserId(userId));
    }

    @DeleteMapping("/delete/{budgetId}")
    public ResponseEntity<String> deleteBudget(@PathVariable String budgetId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        budgetService.deleteBudget(userId, budgetId);
        return ResponseEntity.noContent().build();
    }

}
