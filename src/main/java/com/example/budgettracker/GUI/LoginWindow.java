package com.example.budgettracker.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginWindow {

    public LoginWindow() {
        // Create the login frame
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridLayout(5, 2, 10, 10));

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");
        JButton signUpButton = new JButton("Sign Up");

        // Add components to the frame
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(new JLabel()); // Empty space
        frame.add(new JLabel()); // Empty space
        frame.add(loginButton);
        frame.add(signUpButton);
        frame.add(new JLabel()); // Empty space
        frame.add(exitButton);

        // Action for the Login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Attempt login and get JWT token
            String jwtToken = sendLoginRequest(username, password);
            if (jwtToken != null) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.dispose();
                new MainWindow(jwtToken); // Navigate to Main Window with token
            } else {
                JOptionPane.showMessageDialog(frame, "Login Failed! Invalid credentials.");
            }
        });

        // Action for the Sign-Up button
        signUpButton.addActionListener(e -> {
            frame.dispose();
            new SignUpWindow(); // Navigate to Sign-Up window
        });

        // Action for Exit button
        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        // Center and display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String cleanToken(String rawResponse) {
        // Extract the token value from the raw JSON response
        return rawResponse.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
    }

    /**
     * Method to send the login request to the backend and retrieve JWT token.
     *
     * @param username User's input username
     * @param password User's input password
     * @return JWT token if login is successful; null otherwise
     */
    private String sendLoginRequest(String username, String password) {
        try {
            // API call setup
            URL url = new URL("http://localhost:8080/api/auth/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            // Check response
            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                // Clean the token
                return cleanToken(response.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}
