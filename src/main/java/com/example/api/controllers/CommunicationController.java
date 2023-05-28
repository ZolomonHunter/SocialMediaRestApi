package com.example.api.controllers;

import com.example.api.services.JwtService;
import com.example.api.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JwtAuth")
public class CommunicationController {
    private final UserService userService;
    private final JwtService jwtService;
    @PostMapping("sendFriendRequest")
    public ResponseEntity<String> sendFriendRequest(String username, @RequestHeader(name="Authorization") String token) {
//        var name = jwtService.extractUsername(SecurityContextHolder.getContext().getAuthentication().getDetails());
        return ResponseEntity.ok("");
    }
}
