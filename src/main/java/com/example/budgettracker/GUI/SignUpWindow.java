package com.example.budgettracker.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpWindow {

    private JFrame frame; // Make the frame accessible across methods

    public SignUpWindow() {
        frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton signUpButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back");

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(signUpButton);
        frame.add(backButton);

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            registerUser(username, email, password);
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new LoginWindow(); // Navigate back to LoginWindow
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void registerUser(String username, String email, String password) {
        try {
            URL url = new URL("http://localhost:8080/api/auth/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format(
                    "{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
                    username, email, password
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes("utf-8"));
            }

            int statusCode = conn.getResponseCode();
            if (statusCode == 200) {
                JOptionPane.showMessageDialog(frame, "Registration Successful! Please log in.");
                frame.dispose(); // Close the Sign-Up window
                new LoginWindow(); // Open the Login window
            } else {
                JOptionPane.showMessageDialog(frame, "Registration Failed! Username or email may be taken.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpWindow::new);
    }
}
