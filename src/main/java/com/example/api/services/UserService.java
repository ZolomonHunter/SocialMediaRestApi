package com.example.api.services;

import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    static class UserNotFoundException extends RuntimeException { }

    public User add(User user) {
        return userRepository.save(user);
    }

    public User getCurrentUser() {
        return get(jwtService.getCurrentUsername());
    }

    public User get(int id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User get(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public boolean isUserWithUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isUserWithEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
