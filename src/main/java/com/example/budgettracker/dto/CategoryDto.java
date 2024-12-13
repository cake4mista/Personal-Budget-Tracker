package com.example.budgettracker.dto;

public class CategoryDto {
    private String name;

    // Constructors
    public CategoryDto() {}

    public CategoryDto(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
