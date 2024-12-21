package com.example.budgettracker.dto;

public class ExpenseDto {
    private String date;        // Date of the expense
    private String category;    // Category (e.g., Food, Transport)
    private String description; // Expense description
    private Double amount;      // Expense amount

    // Getters and Setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
