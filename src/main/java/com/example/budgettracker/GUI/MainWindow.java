package com.example.budgettracker.GUI;

import javax.swing.*;
import java.awt.*;

public class MainWindow {

    private String jwtToken; // Store JWT token

    public MainWindow(String token) {
        this.jwtToken = token; // Pass JWT token after successful login

        // Create the main frame
        JFrame frame = new JFrame("Personal Budget Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column, spacing

        // Create buttons for navigation
        JButton budgetButton = new JButton("Manage Budgets");
        JButton expenseButton = new JButton("Manage Expenses");
        JButton categoryButton = new JButton("Manage Categories");
        JButton logoutButton = new JButton("Logout");
        JButton exitButton = new JButton("Exit");

        // Action listener for Budget Management
        budgetButton.addActionListener(e -> {
            frame.dispose();
            new BudgetWindow(jwtToken); // Open Budget Management Window
        });

        // Action listener for Expense Management
        expenseButton.addActionListener(e -> {
            frame.dispose();
            new ExpenseWindow(jwtToken); // Open Expense Management Window
        });

        // Action listener for Category Management
        categoryButton.addActionListener(e -> {
            frame.dispose();
            new CategoryWindow(jwtToken); // Open Category Management Window
        });

        // Action listener for Logout
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginWindow(); // Return to LoginWindow
        });

        // Action listener for Exit button
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the frame
        frame.add(budgetButton);
        frame.add(expenseButton);
        frame.add(categoryButton);
        frame.add(logoutButton);
        frame.add(exitButton);

        // Place the frame at the center of the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Entry point for MainWindow with token passed after Login
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow("your_jwt_token_here"));
    }
}
