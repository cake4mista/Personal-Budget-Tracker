package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;

    private String userId;      // Derived from JWT
    private String date;        // Date of the expense
    private String category;    // Category (e.g., Food, Transport)
    private String description; // Expense description
    private Double amount;      // Expense amount

    // Constructors
    public Expense() {}

    public Expense(String userId, String date, String category, String description, Double amount) {
        this.userId = userId;
        this.date = date;
        this.category = category;
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

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
