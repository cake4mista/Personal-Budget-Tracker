package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.YearMonth;

@Document(collection = "budgets")
public class Budget {

    @Id
    private String id;

    private Double monthlyLimit;

    private YearMonth budgetMonth;

    public Budget() {}

    public Budget(Double monthlyLimit, YearMonth budgetMonth) {
        this.monthlyLimit = monthlyLimit;
        this.budgetMonth = budgetMonth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(Double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public YearMonth getBudgetMonth() {
        return budgetMonth;
    }

    public void setBudgetMonth(YearMonth budgetMonth) {
        this.budgetMonth = budgetMonth;
    }
}
