package com.spring_security.spring_security_app.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    // It has all built-in methods
}
