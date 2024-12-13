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

        String[] columnNames = {"ID", "Description", "Amount", "Date"};
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

    private void fetchExpenses() {
        try {
            URL url = new URL("http://localhost:8080/api/expenses");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

            Scanner scanner = new Scanner(conn.getInputStream());
            tableModel.setRowCount(0);
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                tableModel.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error fetching expenses: " + e.getMessage());
        }
    }

    private void addExpense() {
        try {
            String description = JOptionPane.showInputDialog("Enter Description:");
            String amount = JOptionPane.showInputDialog("Enter Amount:");
            String date = JOptionPane.showInputDialog("Enter Date (YYYY-MM-DD):");

            URL url = new URL("http://localhost:8080/api/expenses");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"description\":\"%s\", \"amount\":%s, \"date\":\"%s\"}", description, amount, date);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
            }

            fetchExpenses();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding expense: " + e.getMessage());
        }
    }

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

            fetchExpenses();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting expense: " + e.getMessage());
        }
    }
}
