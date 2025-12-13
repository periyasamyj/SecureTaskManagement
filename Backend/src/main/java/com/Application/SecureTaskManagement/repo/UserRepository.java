package com.Application.SecureTaskManagement.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Application.SecureTaskManagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
