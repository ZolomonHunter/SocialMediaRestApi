package com.example.api.controllers;

import com.example.api.models.FriendRequest;
import com.example.api.models.FriendRequestStatus;
import com.example.api.models.User;
import com.example.api.repositories.FriendRequestRepository;
import com.example.api.services.CommunicationService;
import com.example.api.services.FriendRequestService;
import com.example.api.services.JwtService;
import com.example.api.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JwtAuth")
public class CommunicationController {
    private final CommunicationService communicationService;

    @PostMapping("sendFriendRequest")
    public ResponseEntity<String> sendFriendRequest(String receiverUsername) {
        return ResponseEntity.ok(communicationService.sendFriendRequest(receiverUsername));
    }


    @ExceptionHandler(CommunicationService.FriendRequestAlreadySent.class)
    public ResponseEntity<?> friendRequestAlreadySent(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>("You already sent friend request to that user", HttpStatus.CONFLICT);
    }

}
