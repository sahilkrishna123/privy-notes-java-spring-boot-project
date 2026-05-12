package com.spring_security.spring_security_app.user;

import com.spring_security.spring_security_app.dto.UserRequest;
import com.spring_security.spring_security_app.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(
        name ="User Management CRUD Operations",
        description = "You can create, update, delete, and get user."
)
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // Get all
    @GetMapping
    public List<User> getAll(){
        return repository.findAll();
    }
    // Get one
    @GetMapping("/{username}")
    public User getOne(@PathVariable String username){
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    // Create User
    @PostMapping
    public ResponseEntity<UserResponse>  createUser(@RequestBody UserRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        User savedUser = repository.save(user);

        UserResponse response = new UserResponse(savedUser.getUsername(), savedUser.getRole());
        return ResponseEntity.status(201).body(response);
    }
}
