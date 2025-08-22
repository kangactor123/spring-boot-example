package com.boot.demo1.user.repository;

import com.boot.demo1.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
//    User signUp(String userName, String password, String role);
}