package com.transpo.controller;

import com.transpo.dto.*;
import com.transpo.model.User;
import com.transpo.repository.UserRepository;
import com.transpo.security.JwtUtil;
import com.transpo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepo;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            User created = userService.register(req);
            return ResponseEntity.ok("Registered user id: " + created.getId());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<User> userOpt = userRepo.findByUsername(req.getUsername());
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
        User u = userOpt.get();
        if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) return ResponseEntity.status(401).body("Invalid credentials");
        String token = jwtUtil.generateToken(u.getUsername(), u.getId(), u.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, u.getUsername(), u.getRole().name(), u.getId()));
    }
}
