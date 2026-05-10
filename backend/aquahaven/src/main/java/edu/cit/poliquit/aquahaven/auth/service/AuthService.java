package edu.cit.poliquit.aquahaven.auth.service;

import edu.cit.poliquit.aquahaven.auth.dto.request.LoginRequest;
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

        String accessToken  = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateToken(user.getEmail());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getEmail(), user.getFirstname(), user.getLastname(), user.getRole()
        );

        return AuthResponse.ok(userInfo, accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.fail("AUTH-001", "Invalid credentials", null);
        }

        String accessToken  = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateToken(user.getEmail());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getEmail(), user.getFirstname(), user.getLastname(), user.getRole()
        );

        return AuthResponse.ok(userInfo, accessToken, refreshToken);
    }
}