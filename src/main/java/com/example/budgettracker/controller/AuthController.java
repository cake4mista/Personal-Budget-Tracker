package com.example.budgettracker.controller;

import com.example.budgettracker.model.User;
import com.example.budgettracker.security.JWTGenerator;
import com.example.budgettracker.service.UserService;
import com.example.budgettracker.dto.LoginRequest;
import com.example.budgettracker.dto.UserDto;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(UserService userService, JWTGenerator jwtGenerator) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername()) || userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Username or email already taken!");
        }

        User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getPassword(), "USER");
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByUsername(loginRequest.getUsername());

        if (user != null && BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtGenerator.generateToken(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), null));
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("Invalid username or password.");
    }
}
