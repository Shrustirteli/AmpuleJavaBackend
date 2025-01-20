package com.company.service;

import com.company.model.User;
import com.company.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Autowire PasswordEncoder

    // Existing method to save a user
    public void save(User user) {
        userRepository.save(user);
    }

    // Existing method to find a user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // New method for resetting password
    public boolean resetPassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email.toLowerCase());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Hashing the new password before saving
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword); 
            userRepository.save(user); 
            return true;
        } else {
            throw new RuntimeException("User not found");
        }
    }


    // Method for password hashing using BCrypt
    private String hashPassword(String password) {
        return passwordEncoder.encode(password); // Use BCryptPasswordEncoder to hash password
    }
}
