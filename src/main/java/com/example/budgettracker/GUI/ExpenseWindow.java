package com.example.budgettracker.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ExpenseWindow {
    private String jwtToken;
    private JTable expenseTable;
    private DefaultTableModel tableModel;

    public ExpenseWindow(String token) {
        this.jwtToken = cleanToken(token); // Clean the token
        System.out.println("Cleaned JWT Token: " + jwtToken);

        JFrame frame = new JFrame("Expense Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        String[] columnNames = {"ID", "Description", "Amount", "Date", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0);
        expenseTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        JButton fetchButton = new JButton("Fetch Expenses");
        JButton addButton = new JButton("Add Expense");
        JButton deleteButton = new JButton("Delete Expense");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fetchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        fetchButton.addActionListener(e -> fetchExpenses());
        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Utility method to clean the token by removing unexpected formatting.
     *
     * @param token Raw token string
     * @return Cleaned token string
     */
    private String cleanToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        return token.replaceAll("\\s+", "").replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
    }

    /**
     * Fetches expenses from the server and populates the table.
     */
    private void fetchExpenses() {
        try {
            URL url = new URL("http://localhost:8080/api/expenses");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonResponse.append(scanner.nextLine());
            }
            scanner.close();

            // Parse the JSON response
            org.json.JSONArray expenses = new org.json.JSONArray(jsonResponse.toString());
            tableModel.setRowCount(0); // Clear the table
            for (int i = 0; i < expenses.length(); i++) {
                org.json.JSONObject expense = expenses.getJSONObject(i);
                tableModel.addRow(new Object[]{
                        expense.getString("id"),
                        expense.getString("description"),
                        expense.getDouble("amount"),
                        expense.getString("date"),
                        expense.getString("category")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching expenses: " + e.getMessage());
        }
    }

    /**
     * Adds a new expense entry to the server.
     */
    private void addExpense() {
        try {
            String description = JOptionPane.showInputDialog("Enter Description:");
            String amountStr = JOptionPane.showInputDialog("Enter Amount:");
            String date = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");

            // Categories as dropdown options
            String[] categories = {"Food", "Transport", "Utilities", "Entertainment", "Other"};
            String category = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Category:",
                    "Category",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    categories,
                    categories[0]
            );

            // Validate input
            if (description == null || description.trim().isEmpty() ||
                    amountStr == null || amountStr.trim().isEmpty() ||
                    date == null || date.trim().isEmpty() ||
                    category == null) {
                JOptionPane.showMessageDialog(null, "All fields are required.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a numeric value.");
                return;
            }

            // Prepare JSON payload
            String jsonInput = String.format(
                    "{\"description\":\"%s\", \"amount\":%.2f, \"date\":\"%s\", \"category\":\"%s\"}",
                    description, amount, date, category
            );

            // Send the POST request
            URL url = new URL("http://localhost:8080/api/expenses");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Expense added successfully!");
                fetchExpenses();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add expense. Response code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding expense: " + e.getMessage());
        }
    }

    /**
     * Deletes the selected expense entry from the server.
     */
    private void deleteExpense() {
        try {
            int selectedRow = expenseTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Select an expense to delete!");
                return;
            }

            String id = tableModel.getValueAt(selectedRow, 0).toString();
            URL url = new URL("http://localhost:8080/api/expenses/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Expense deleted successfully!");
                fetchExpenses();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete expense. Response code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting expense: " + e.getMessage());
        }
    }
}
