package com.example.hospital.service;

import com.example.hospital.dto.auth.AuthResponse;
import com.example.hospital.dto.auth.LoginRequest;
import com.example.hospital.dto.auth.RegisterRequest;
import com.example.hospital.model.User;
import com.example.hospital.model.enums.Role;
import com.example.hospital.repository.UserRepo;
import com.example.hospital.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .build();

        userRepo.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        // Try to find user by email first, then by name
        User user = userRepo.findByEmail(request.getEmail())
                .orElse(userRepo.findByName(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("Invalid credentials")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
        return new AuthResponse(token, user.getRole().name());
    }
}