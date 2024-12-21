package com.example.budgettracker.dto;

public class BudgetDto {
    private String date;    // Date in YYYY-MM format
    private Double amount;  // Budget limit

    // Getters and Setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
