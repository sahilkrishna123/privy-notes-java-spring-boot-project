package com.spring_security.spring_security_app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping("/hello-user")
    public String helloUser(){
        return "Hello From User";
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user){
        return repository.save(user);
    }
}
//User user = new User();
//user.setUsername("sahil");
//user.setPassword("1234");
//user.setRole(Role.ROLE_USER);
//
//userRepository.save(user);