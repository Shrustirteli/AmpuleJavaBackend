package com.company.controller;

import com.company.config.JwtUtil;
import com.company.model.Details;
import com.company.model.User;
import com.company.repository.DetailsRepository;
import com.company.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DetailsRepository detailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil bean

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/details")
    public String saveDetails(@RequestBody Details details, @RequestHeader("Authorization") String token) {
        // Validate and extract token
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header format");
        }

        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        String email = jwtUtil.extractEmail(jwtToken); // Extract email from token

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        details.setUser(user); // Associate the details with the authenticated user
        detailsRepository.save(details); // Save details to the repository

        return "Details saved successfully!";
    }
}
