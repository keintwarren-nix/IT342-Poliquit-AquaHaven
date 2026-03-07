package edu.cit.poliquit.aquahaven.service;

import edu.cit.poliquit.aquahaven.dto.*;
import edu.cit.poliquit.aquahaven.entity.User;
import edu.cit.poliquit.aquahaven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, null, "Email already registered");
        }

        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("firstname", user.getFirstname());
        data.put("lastname", user.getLastname());
        data.put("accessToken", token);

        return new AuthResponse(true, data, null);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, null, "Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("firstname", user.getFirstname());
        data.put("lastname", user.getLastname());
        data.put("role", user.getRole());
        data.put("accessToken", token);

        return new AuthResponse(true, data, null);
    }
}