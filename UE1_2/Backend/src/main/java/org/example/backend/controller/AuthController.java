package org.example.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthController {


    @PostMapping("/login-test")
    public String testLogin(@RequestParam String username, @RequestParam String password) {
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        return "ok";
    }

    @GetMapping("/checkLogin")
    public ResponseEntity<?> checkLogin(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        var roles = authentication.getAuthorities()
                .stream()
                .map(granted -> granted.getAuthority())
                .toList();

        var response = Map.of(
                "username", authentication.getName(),
                "roles", roles
        );

        return ResponseEntity.ok(response);
    }
}

