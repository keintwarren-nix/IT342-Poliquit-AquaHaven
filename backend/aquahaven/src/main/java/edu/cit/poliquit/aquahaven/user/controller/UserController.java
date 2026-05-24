package edu.cit.poliquit.aquahaven.user.controller;

import edu.cit.poliquit.aquahaven.common.response.ApiResponse;
import edu.cit.poliquit.aquahaven.user.dto.request.ChangePasswordRequest;
import edu.cit.poliquit.aquahaven.user.dto.request.UpdateProfileRequest;
import edu.cit.poliquit.aquahaven.user.dto.response.UserProfileResponse;
import edu.cit.poliquit.aquahaven.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ApiResponse<UserProfileResponse> getProfile(Authentication authentication) {
        UserProfileResponse profile = userService.getProfile(authentication);
        return ApiResponse.ok(profile);
    }

    @PutMapping("/profile")
    public ApiResponse<UserProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request,
                                                           Authentication authentication) {
        UserProfileResponse profile = userService.updateProfile(request, authentication);
        return ApiResponse.ok(profile);
    }

    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request,
                                             Authentication authentication) {
        userService.changePassword(request, authentication);
        return ApiResponse.ok(null);
    }
}
