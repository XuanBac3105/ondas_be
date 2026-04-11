package com.example.ondas_be.domain.repoport;

import com.example.ondas_be.domain.entity.User;

import java.util.Optional;

public interface UserRepoPort {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
}
