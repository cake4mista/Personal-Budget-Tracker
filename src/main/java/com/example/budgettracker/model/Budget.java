package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "budgets")
public class Budget {
    @Id
    private String id;

    private String userId;     // Derived from JWT
    private String date;       // Date in YYYY-MM format
    private String description; // Description for the budget
    private Double amount;     // Budget limit

    // Constructors
    public Budget() {}

    public Budget(String userId, String date, String description, Double amount) {
        this.userId = userId;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
