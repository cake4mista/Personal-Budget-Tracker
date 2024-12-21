package com.example.budgettracker.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class BudgetWindow {
    private String jwtToken;
    private JTable budgetTable;
    private DefaultTableModel tableModel;

    public BudgetWindow(String token) {
        this.jwtToken = cleanToken(token); // Clean the token
        System.out.println("Cleaned JWT Token: " + jwtToken);

        JFrame frame = new JFrame("Budget Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);

        String[] columnNames = {"ID", "Date", "Amount"};
        tableModel = new DefaultTableModel(columnNames, 0);
        budgetTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(budgetTable);
        JButton fetchButton = new JButton("Fetch Budgets");
        JButton addButton = new JButton("Add Budget");
        JButton deleteButton = new JButton("Delete Budget");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fetchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        fetchButton.addActionListener(e -> fetchBudgets());
        addButton.addActionListener(e -> addBudget());
        deleteButton.addActionListener(e -> deleteBudget());

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
     * Fetches budgets from the server and populates the table.
     */
    private void fetchBudgets() {
        try {
            URL url = new URL("http://localhost:8080/api/budgets/get");
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
            org.json.JSONArray budgets = new org.json.JSONArray(jsonResponse.toString());
            tableModel.setRowCount(0); // Clear the table
            for (int i = 0; i < budgets.length(); i++) {
                org.json.JSONObject budget = budgets.getJSONObject(i);
                tableModel.addRow(new Object[]{
                        budget.getString("id"),
                        budget.getString("date"),
                        budget.getDouble("amount")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching budgets: " + e.getMessage());
        }
    }

    /**
     * Adds a new budget entry to the server.
     */
    private void addBudget() {
        try {
            // Predefined months and years
            String[] months = {"January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"};
            String[] years = {"2024", "2025", "2026"};

            // Month selection dropdown
            JComboBox<String> monthBox = new JComboBox<>(months);
            JComboBox<String> yearBox = new JComboBox<>(years);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(2, 2));
            panel.add(new JLabel("Select Month:"));
            panel.add(monthBox);
            panel.add(new JLabel("Select Year:"));
            panel.add(yearBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add Budget",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return; // Exit if user cancels
            }

            // Get selected month and year
            int monthIndex = monthBox.getSelectedIndex() + 1; // Convert to 1-12
            String year = (String) yearBox.getSelectedItem();
            String month = String.format("%02d", monthIndex); // Format month as 01, 02, etc.

            String date = year + "-" + month; // Combine into YYYY-MM

            // Budget amount input
            String amountStr = JOptionPane.showInputDialog("Enter Budget Amount:");
            if (amountStr == null || amountStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Amount is required.");
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
                    "{\"date\":\"%s\", \"amount\":%.2f}",
                    date, amount
            );

            // Send the POST request
            URL url = new URL("http://localhost:8080/api/budgets/create");
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
                JOptionPane.showMessageDialog(null, "Budget added successfully!");
                fetchBudgets();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add budget. Response code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding budget: " + e.getMessage());
        }
    }

    private void deleteBudget() {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a budget to delete.");
            return;
        }

        // Get the ID of the selected budget
        String budgetId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this budget?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return; // Exit if user cancels
        }

        try {
            URL url = new URL("http://localhost:8080/api/budgets/delete/" + budgetId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                JOptionPane.showMessageDialog(null, "Budget deleted successfully!");
                fetchBudgets(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete budget. Response code: " + responseCode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting budget: " + e.getMessage());
        }
    }

}
