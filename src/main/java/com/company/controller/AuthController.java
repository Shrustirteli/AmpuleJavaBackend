package com.company.controller;

import com.company.config.JwtUtil;
import com.company.model.ResetPasswordRequest;
import com.company.model.User;
import com.company.repository.UserRepository;
import com.company.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService; // Injecting UserService to handle reset password

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Check if user already exists in the database
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "User already registered!";
        }

        // Encode the password and save the new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user, HttpServletRequest request) {
        // Get the browser name
        String userAgent = request.getHeader("User-Agent");
        String browserName = getBrowserName(userAgent);
        logger.info("Login attempt from browser: " + browserName);

        // Check if user exists in the database
        User storedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate the password
        if (passwordEncoder.matches(user.getPassword(), storedUser.getPassword())) {
            // Update the browser name in the user entity
            storedUser.setBrowserName(browserName);
            userRepository.save(storedUser); // Save the updated entity to the database

            // Generate the JWT token
            String token = jwtUtil.generateToken(storedUser);

            // Prepare the response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private String getBrowserName(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        userAgent = userAgent.toLowerCase(); // Normalize for case-insensitivity

        if (userAgent.contains("chrome") && !userAgent.contains("edge")) return "Edge";
        if (userAgent.contains("firefox")) return "Firefox";
        if (userAgent.contains("safari") && !userAgent.contains("chrome")) return "Safari";
        if (userAgent.contains("edge")) return "Edge";
        if (userAgent.contains("postman")) return "Postman";
        return "Unknown";
    }


    // Add the Reset Password endpoint
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email must not be null or empty.");
        }

        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("New password must not be null or empty.");
        }

        try {
            userService.resetPassword(email, newPassword);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password.");
        }
    }
    
    
}
