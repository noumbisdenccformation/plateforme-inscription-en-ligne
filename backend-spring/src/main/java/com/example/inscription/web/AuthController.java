package com.example.inscription.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
		String email = body.getOrDefault("email", "");
		String password = body.getOrDefault("password", "");
		if (email.isBlank() || password.isBlank()) {
			return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
		}
		return ResponseEntity.ok(Map.of("token", "mock-jwt-token"));
	}

	@GetMapping("/me")
	public ResponseEntity<?> me() {
		return ResponseEntity.ok(Map.of("user", Map.of(
			"id", "1",
			"email", "admin@example.com",
			"role", "ADMIN"
		)));
	}
} 