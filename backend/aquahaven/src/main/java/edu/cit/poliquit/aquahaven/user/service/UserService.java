package edu.cit.poliquit.aquahaven.user.service;

import edu.cit.poliquit.aquahaven.common.exception.BadRequestException;
import edu.cit.poliquit.aquahaven.common.exception.ResourceNotFoundException;
import edu.cit.poliquit.aquahaven.user.dto.request.ChangePasswordRequest;
import edu.cit.poliquit.aquahaven.user.dto.request.UpdateProfileRequest;
import edu.cit.poliquit.aquahaven.user.dto.response.UserProfileResponse;
import edu.cit.poliquit.aquahaven.user.entity.User;
import edu.cit.poliquit.aquahaven.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String formatRole(String role) {
        if (role == null) return "CUSTOMER";
        return role.startsWith("ROLE_") ? role.substring(5) : role;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFirstname() != null && !request.getFirstname().isBlank()) {
            user.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null && !request.getLastname().isBlank()) {
            user.setLastname(request.getLastname());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        user = userRepository.save(user);
        return toProfileResponse(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (request.getNewPassword().length() < 6) {
            throw new BadRequestException("New password must be at least 6 characters long");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserProfileResponse toProfileResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setPhone(user.getPhone());
        response.setRole(formatRole(user.getRole()));
        return response;
    }
}
