package com.example.inscription.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/dossiers")
public class DossiersController {

	@GetMapping
	public ResponseEntity<?> list(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize
	) {
		List<Map<String, Object>> items = new ArrayList<>();
		String[] states = new String[]{"PENDING", "APPROVED", "REJECTED"};
		Random rnd = new Random();
		for (int i = 0; i < pageSize; i++) {
			int id = (page - 1) * pageSize + i + 1;
			Map<String, Object> obj = new HashMap<>();
			obj.put("id", id);
			obj.put("candidat", "Candidat " + id);
			obj.put("completude", rnd.nextInt(100));
			obj.put("etat", states[rnd.nextInt(states.length)]);
			obj.put("deposeLe", Instant.ofEpochMilli(System.currentTimeMillis() - (long) (rnd.nextDouble() * 1e10)).toString());
			items.add(obj);
		}
		return ResponseEntity.ok(Map.of("items", items, "total", 200));
	}
} 