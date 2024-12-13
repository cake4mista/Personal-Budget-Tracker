package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "budgets") // Ensure collection name matches exactly
public class Budget {
    @Id
    private String id;
    private String username;
    private String month;
    private double amount;

    // Constructors
    public Budget() {}

    public Budget(String username, String month, double amount) {
        this.username = username;
        this.month = month;
        this.amount = amount;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
