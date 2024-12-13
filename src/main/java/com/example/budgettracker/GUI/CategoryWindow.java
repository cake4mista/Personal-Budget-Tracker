package com.example.budgettracker.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CategoryWindow {
    private String jwtToken;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    public CategoryWindow(String token) {
        this.jwtToken = cleanToken(token); // Clean the token before using it
        System.out.println("Cleaned JWT Token: " + jwtToken);

        JFrame frame = new JFrame("Category Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        String[] columnNames = {"ID", "Name"};
        tableModel = new DefaultTableModel(columnNames, 0);
        categoryTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        JButton fetchButton = new JButton("Fetch Categories");
        JButton addButton = new JButton("Add Category");
        JButton deleteButton = new JButton("Delete Category");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fetchButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        fetchButton.addActionListener(e -> fetchCategories());
        addButton.addActionListener(e -> addCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Utility method to clean the token by removing unnecessary formatting.
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

    private void fetchCategories() {
        try {
            URL url = new URL("http://localhost:8080/api/categories");
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
            JOptionPane.showMessageDialog(null, "Error fetching categories: " + e.getMessage());
        }
    }

    private void addCategory() {
        try {
            String name = JOptionPane.showInputDialog("Enter Category Name:");

            URL url = new URL("http://localhost:8080/api/categories");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"name\":\"%s\"}", name);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
            }

            fetchCategories();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding category: " + e.getMessage());
        }
    }

    private void deleteCategory() {
        try {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Select a category to delete!");
                return;
            }

            String id = tableModel.getValueAt(selectedRow, 0).toString();
            URL url = new URL("http://localhost:8080/api/categories/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Bearer " + jwtToken);

            fetchCategories();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting category: " + e.getMessage());
        }
    }
}
