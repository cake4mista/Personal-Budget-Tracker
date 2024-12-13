package com.example.budgettracker.service;

import com.example.budgettracker.model.User;

public interface UserService {
    User registerUser(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User getUserByUsername(String username);
}
