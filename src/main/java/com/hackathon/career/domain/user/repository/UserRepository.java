package com.hackathon.career.domain.user.repository;

import com.hackathon.career.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByUsername(String username);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByUsername(String username);
}