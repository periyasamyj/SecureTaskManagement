package com.Application.SecureTaskManagement.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Application.SecureTaskManagement.dto.Response;
import com.Application.SecureTaskManagement.dto.UserRequest;
import com.Application.SecureTaskManagement.entity.User;
import com.Application.SecureTaskManagement.enums.Role;
import com.Application.SecureTaskManagement.exceptions.BadRequestException;
import com.Application.SecureTaskManagement.exceptions.NotFoundException;
import com.Application.SecureTaskManagement.repo.UserRepository;
import com.Application.SecureTaskManagement.security.JwtUtils;
import com.Application.SecureTaskManagement.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Manual logger instead of @Slf4j
    private static final Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public Response<?> signUp(UserRequest userRequest) {
        log.info("Inside signUp()");
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());

        if (existingUser.isPresent()) {
            throw new BadRequestException("Username already taken");
        }

        User user = new User();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // save the user
        userRepository.save(user);

        Response<Object> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("User registered successfully");

        return response;
    }

    @Override
    public Response<?> login(UserRequest userRequest) {
        log.info("Inside login()");

        User user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Password");
        }

        String token = jwtUtils.generateToken(user.getUsername());

        Response<String> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Login successful");
        response.setData(token);

        return response;
    }

    @Override
    public User getCurrentLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
