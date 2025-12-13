package com.Application.SecureTaskManagement.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Application.SecureTaskManagement.entity.User;
import com.Application.SecureTaskManagement.exceptions.NotFoundException;
import com.Application.SecureTaskManagement.repo.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // ✅ Manual constructor (replacement for @RequiredArgsConstructor)
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // ✅ Use manual constructor instead of builder
        return new AuthUser(user);
    }
}
