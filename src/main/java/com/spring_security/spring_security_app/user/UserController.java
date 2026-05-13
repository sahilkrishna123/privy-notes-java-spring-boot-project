package com.spring_security.spring_security_app.user;

import com.spring_security.spring_security_app.user.dto.PasswordUpdateRequest;
import com.spring_security.spring_security_app.user.dto.UserResponse;
import com.spring_security.spring_security_app.user.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(
        name = "User Management",
        description = "Create, read, update, and delete users."
)
public class UserController {

    @Autowired
    private UserService userService;

    // GET /users — admin sees all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /users/me — logged-in user sees their own profile
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get my profile")
    public ResponseEntity<UserResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    // GET /users/{username} — admin only
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get a user by username (Admin only)")
    public ResponseEntity<UserResponse> getOne(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    // POST /users/register — public, no auth required
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(201).body(userService.createUser(request));
    }

    // PATCH /users/me/password — user updates own password
    @PatchMapping("/me/password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Update my password")
    public ResponseEntity<UserResponse> updateMyPassword(@RequestBody PasswordUpdateRequest request) {
        return ResponseEntity.ok(userService.updateMyPassword(request.getNewPassword()));
    }

    // DELETE /users/me — user deletes own account
    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Delete my account")
    public ResponseEntity<Void> deleteMyAccount() {
        userService.deleteMyAccount();
        return ResponseEntity.noContent().build();
    }

    // DELETE /users/{username} — admin deletes anyone
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user by username (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}