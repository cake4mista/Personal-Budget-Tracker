package com.example.budgettracker;

import com.example.budgettracker.GUI.LoginWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false"); // Force disable headless mode
        SpringApplication.run(Main.class, args);
        System.out.println("Personal Budget Tracker Application is running...");
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }

}
