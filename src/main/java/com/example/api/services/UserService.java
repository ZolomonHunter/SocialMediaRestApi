package com.example.api.services;

import com.example.api.models.User;
import com.example.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    static class UserNotFoundException extends RuntimeException { }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public boolean isUserWithUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isUserWithEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
