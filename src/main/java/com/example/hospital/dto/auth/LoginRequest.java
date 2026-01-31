package com.example.hospital.dto.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email or username is required")
    @JsonAlias({"username", "email"})
    private String email; // Can be email or username/name

    @NotBlank(message = "Password is required")
    private String password;
}