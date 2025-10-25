package com.transpo.service;

import com.transpo.dto.RegisterRequest;
import com.transpo.model.User;
import com.transpo.model.Role;
import com.transpo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    public User register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) throw new RuntimeException("Email already used");
        if (userRepo.existsByUsername(req.getUsername())) throw new RuntimeException("Username already used");
        if (!req.getPassword().equals(req.getPasswordConfirm())) throw new RuntimeException("Passwords do not match");

        User u = new User();
        u.setEmail(req.getEmail());
        u.setUsername(req.getUsername());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        u.setRole(Role.valueOf(req.getRole().toUpperCase()));
        return userRepo.save(u);
    }
}
