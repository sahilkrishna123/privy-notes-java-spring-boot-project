package com.spring_security.spring_security_app.user;
import com.spring_security.spring_security_app.user.dto.UserResponse;
import com.spring_security.spring_security_app.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ── Helpers ──────────────────────────────────────────────

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private User resolveUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + username));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getUsername(), user.getRole());
    }

    // ── Operations ───────────────────────────────────────────

    // Get all users — admin only
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get one user by username — admin only
    public UserResponse getUser(String username) {
        return toResponse(resolveUser(username));
    }

    // Get currently logged-in user's profile
    public UserResponse getMyProfile() {
        return toResponse(resolveUser(getCurrentUsername()));
    }

    // Register/create user — public endpoint
    public UserResponse createUser(UserRequest request) {
        // Prevent duplicate usernames
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already taken: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Default to USER role if not specified — clients should never set ADMIN themselves
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        return toResponse(userRepository.save(user));
    }

    // Update own password — authenticated user
    public UserResponse updateMyPassword(String newRawPassword) {
        if (newRawPassword == null || newRawPassword.length() < 6) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password must be at least 6 characters");
        }
        User user = resolveUser(getCurrentUsername());
        user.setPassword(passwordEncoder.encode(newRawPassword));
        return toResponse(userRepository.save(user));
    }

    // Admin deletes anyone
    public void deleteUser(String username) {
        User user = resolveUser(username);
        userRepository.delete(user);
    }

    // User deletes their own account
    public void deleteMyAccount() {
        User user = resolveUser(getCurrentUsername());
        userRepository.delete(user);
    }
}