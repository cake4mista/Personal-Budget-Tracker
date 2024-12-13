package com.example.budgettracker.dto;

public class BudgetDto {
    private String month;
    private double amount;

    // Constructors
    public BudgetDto() {}

    public BudgetDto(String month, double amount) {
        this.month = month;
        this.amount = amount;
    }

    // Getters and Setters
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
