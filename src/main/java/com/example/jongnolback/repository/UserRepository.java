package com.example.jongnolback.repository;

import com.example.jongnolback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String username);

    long countByUserId(String userId);
}
