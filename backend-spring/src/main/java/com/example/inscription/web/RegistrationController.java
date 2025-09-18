package com.example.inscription.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

	private final Map<String, Map<String, Object>> store = new HashMap<>();

	@GetMapping("/{userId}")
	public ResponseEntity<?> getAll(@PathVariable String userId) {
		return ResponseEntity.ok(store.getOrDefault(userId, Map.of()));
	}

	@PostMapping("/{userId}/{section}")
	public ResponseEntity<?> saveSection(@PathVariable String userId, @PathVariable String section, @RequestBody Map<String, Object> body) {
		store.computeIfAbsent(userId, k -> new HashMap<>()).put(section, body);
		return ResponseEntity.ok(Map.of("ok", true));
	}
} 