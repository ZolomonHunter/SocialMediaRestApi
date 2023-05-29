package com.example.api.controllers;

import com.example.api.models.FriendRequestResponse;
import com.example.api.services.CommunicationService;
import com.example.api.services.FriendRequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("getReceivedFriendRequests")
    public ResponseEntity<List<FriendRequestResponse>> getReceivedFriendRequests() {
        return ResponseEntity.ok(communicationService.getReceivedFriendRequests());
    }

    @PostMapping("getSentFriendRequests")
    public ResponseEntity<List<FriendRequestResponse>> getSentFriendRequests() {
        return ResponseEntity.ok(communicationService.getSentFriendRequests());
    }

    @PostMapping("acceptFriendRequest")
    public ResponseEntity<String> acceptFriendRequest(String senderUsername) {
        return ResponseEntity.ok(communicationService.acceptFriendRequest(senderUsername));
    }

    @PostMapping("declineFriendRequest")
    public ResponseEntity<String> declineFriendRequest(String senderUsername) {
        return ResponseEntity.ok(communicationService.declineFriendRequest(senderUsername));
    }

    @PostMapping("cancelFriendRequest")
    public ResponseEntity<String> cancelFriendRequest(String receiverUsername) {
        return ResponseEntity.ok(communicationService.cancelFriendRequest(receiverUsername));
    }

    @PostMapping("deleteFriendship")
    public ResponseEntity<String> deleteFriendship(String targetUsername) {
        return ResponseEntity.ok(communicationService.deleteFriendship(targetUsername));
    }

    @ExceptionHandler(CommunicationService.FriendRequestAlreadyDeclined.class)
    public ResponseEntity<?> friendRequestAlreadyDeclined() {
        return new ResponseEntity<>("You already declined friend request from that user", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommunicationService.FriendRequestAlreadySent.class)
    public ResponseEntity<?> friendRequestAlreadySent() {
        return new ResponseEntity<>("You already have friend request with that user", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FriendRequestService.FriendRequestNotFound.class)
    public ResponseEntity<?> userNotFound() {
        return new ResponseEntity<>("You didn't received friend request from that user", HttpStatus.NO_CONTENT);
    }

}
