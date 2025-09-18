package com.example.inscription.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@GetMapping("/dashboard")
	public ResponseEntity<?> dashboard() {
		return ResponseEntity.ok(Map.of(
			"totalInscriptions", 1280,
			"pending", 312,
			"approved", 842,
			"rejected", 126,
			"completionByStep", List.of(78, 64, 52, 39, 21)
		));
	}

	@GetMapping("/heatmap")
	public ResponseEntity<?> heatmap() {
		return ResponseEntity.ok(List.of(
			Map.of("day", "Lun", "value", 14),
			Map.of("day", "Mar", "value", 22),
			Map.of("day", "Mer", "value", 35),
			Map.of("day", "Jeu", "value", 28),
			Map.of("day", "Ven", "value", 41),
			Map.of("day", "Sam", "value", 12),
			Map.of("day", "Dim", "value", 9)
		));
	}
} 