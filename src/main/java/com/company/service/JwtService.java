package com.company.service;

import com.company.config.JwtUtil;
import com.company.model.User;
import com.company.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // Constructor-based dependency injection
    @Autowired
    public JwtService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Loads user details from the database by email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Returning UserDetails with email, password, and hardcoded role
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // Role can be dynamic if you store it in DB
                .build();
    }

    // Validates the token by checking email and token expiry
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String email = jwtUtil.extractEmail(token); // Extract email from token
            return (email.equals(userDetails.getUsername()) && !jwtUtil.isTokenExpired(token)); // Validate user and token expiration
        } catch (Exception e) {
            return false; // In case of any exception (e.g., malformed token), return false
        }
    }
}
