package edu.cit.poliquit.aquahaven.controller;

import edu.cit.poliquit.aquahaven.dto.AuthResponse;
import edu.cit.poliquit.aquahaven.dto.LoginRequest;
import edu.cit.poliquit.aquahaven.dto.RegisterRequest;
import edu.cit.poliquit.aquahaven.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}