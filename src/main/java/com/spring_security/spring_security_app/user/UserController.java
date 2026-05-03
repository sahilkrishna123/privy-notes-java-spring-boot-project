package com.spring_security.spring_security_app.user;

import com.spring_security.spring_security_app.dto.UserRequest;
import com.spring_security.spring_security_app.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hello-user")
    public String helloUser(){
        return "Hello From User";
    }

    @GetMapping
    public List<User> getAll(){
        return repository.findAll();
    }


    @PostMapping("/create")
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


//User user = new User();
//user.setUsername("sahil");
//user.setPassword("1234");
//user.setRole(Role.ROLE_USER);
//
//userRepository.save(user);