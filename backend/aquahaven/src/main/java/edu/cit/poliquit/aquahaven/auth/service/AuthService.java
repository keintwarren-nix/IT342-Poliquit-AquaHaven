package edu.cit.poliquit.aquahaven.auth.service;

import edu.cit.poliquit.aquahaven.auth.dto.request.LoginRequest;
import edu.cit.poliquit.aquahaven.auth.dto.request.RefreshTokenRequest;
import edu.cit.poliquit.aquahaven.auth.dto.request.RegisterRequest;
import edu.cit.poliquit.aquahaven.auth.dto.response.AuthResponse;
import edu.cit.poliquit.aquahaven.config.JwtUtil;
import edu.cit.poliquit.aquahaven.user.entity.User;
import edu.cit.poliquit.aquahaven.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
    }

    private String formatRole(String role) {
        if (role == null) return "CUSTOMER";
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.fail("DB-002", "Email already registered", null);
        }

        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_CUSTOMER");

        userRepository.save(user);

        String accessToken  = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getEmail(), user.getFirstname(), user.getLastname(), formatRole(user.getRole())
        );

        return AuthResponse.ok(userInfo, accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.fail("AUTH-001", "Invalid credentials", null);
        }

        String accessToken  = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getEmail(), user.getFirstname(), user.getLastname(), formatRole(user.getRole())
        );

        return AuthResponse.ok(userInfo, accessToken, refreshToken);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (refreshToken == null || !jwtUtil.isTokenValid(refreshToken)) {
            return AuthResponse.fail("AUTH-002", "Invalid or expired refresh token", null);
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return AuthResponse.fail("AUTH-003", "User not found", null);
        }

        String newAccessToken = jwtUtil.generateAccessToken(email);
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getEmail(), user.getFirstname(), user.getLastname(), formatRole(user.getRole())
        );

        return AuthResponse.ok(userInfo, newAccessToken, newRefreshToken);
    }

    public void logout() {
    }
}