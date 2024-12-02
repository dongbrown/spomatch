package com.spomatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/api/auth/status")
    public ResponseEntity<Map<String, Object>> getAuthStatus(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()) {
            response.put("isAuthenticated", true);
            response.put("username", authentication.getName());
        } else {
            response.put("isAuthenticated", false);
        }

        return ResponseEntity.ok(response);
    }
}