package edu.cit.poliquit.aquahaven.auth.controller;

import edu.cit.poliquit.aquahaven.auth.dto.request.LoginRequest;
import edu.cit.poliquit.aquahaven.auth.dto.request.RegisterRequest;
import edu.cit.poliquit.aquahaven.auth.dto.response.AuthResponse;
import edu.cit.poliquit.aquahaven.auth.service.AuthService;
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