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
        this.jwtToken = cleanToken(token);
        System.out.println("Cleaned JWT Token: " + jwtToken);

        JFrame frame = new JFrame("Budget Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        String[] columnNames = {"ID", "Month", "Amount"};
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
     * Utility method to clean the token by removing any unexpected formatting.
     * @param token Raw token string
     * @return Cleaned token string
     */
    private String cleanToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        return token.replaceAll("\\s+", "").replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
    }

    private void fetchBudgets() {
        try {
            URL url = new URL("http://localhost:8080/api/budgets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

            Scanner scanner = new Scanner(conn.getInputStream());
            tableModel.setRowCount(0); // Clear table
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                tableModel.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching budgets: " + e.getMessage());
        }
    }

    private void addBudget() {
        try {
            String month = JOptionPane.showInputDialog("Enter Month:");
            String amount = JOptionPane.showInputDialog("Enter Amount:");

            URL url = new URL("http://localhost:8080/api/budgets");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"month\":\"%s\", \"amount\":%s}", month, amount);
            System.out.println("DEBUG: Sending JSON Payload to Server: " + jsonInput);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
            }

            fetchBudgets();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding budget: " + e.getMessage());
        }
    }

    private void deleteBudget() {
        try {
            int selectedRow = budgetTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Select a budget to delete!");
                return;
            }

            String id = tableModel.getValueAt(selectedRow, 0).toString();
            URL url = new URL("http://localhost:8080/api/budgets/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

            fetchBudgets();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting budget: " + e.getMessage());
        }
    }
}
