package com.spring_security.spring_security_app.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // It has all built-in methods
    Optional<User> findByUsername(String username);
}
