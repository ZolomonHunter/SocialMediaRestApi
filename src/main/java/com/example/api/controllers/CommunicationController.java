package com.example.api.controllers;

import com.example.api.models.AuthenticationResponse;
import com.example.api.models.FriendRequestResponse;
import com.example.api.services.CommunicationService;
import com.example.api.services.FriendRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Sending friend request to target user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request successfully created"),
            @ApiResponse(responseCode = "400", description = "Wrong target user's username"),
            @ApiResponse(responseCode = "409", description = "You already sent request to that user")
    })
    public ResponseEntity<String> sendFriendRequest(String receiverUsername) {
        return ResponseEntity.ok(communicationService.sendFriendRequest(receiverUsername));
    }


    @GetMapping("getReceivedFriendRequests")
    @Operation(summary = "Getting list of received friend requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns list of friend requests",
                    content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FriendRequestResponse.class)) })
    })
    public ResponseEntity<List<FriendRequestResponse>> getReceivedFriendRequests() {
        return ResponseEntity.ok(communicationService.getReceivedFriendRequests());
    }

    @GetMapping("getSentFriendRequests")
    @Operation(summary = "Getting list of sent friend requests")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns list of friend requests",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FriendRequestResponse.class)) })
    })
    public ResponseEntity<List<FriendRequestResponse>> getSentFriendRequests() {
        return ResponseEntity.ok(communicationService.getSentFriendRequests());
    }

    @PostMapping("acceptFriendRequest")
    @Operation(summary = "Accepting friend request from target user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request successfully accepted"),
            @ApiResponse(responseCode = "400", description = "Wrong target user's username")
    })
    public ResponseEntity<String> acceptFriendRequest(String senderUsername) {
        return ResponseEntity.ok(communicationService.acceptFriendRequest(senderUsername));
    }

    @PostMapping("declineFriendRequest")
    @Operation(summary = "Declining friend request from target user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request successfully declined"),
            @ApiResponse(responseCode = "400", description = "Wrong target user's username")
    })
    public ResponseEntity<String> declineFriendRequest(String senderUsername) {
        return ResponseEntity.ok(communicationService.declineFriendRequest(senderUsername));
    }


    @PostMapping("cancelFriendRequest")
    @Operation(summary = "Declining friend request from target user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend request successfully declined"),
            @ApiResponse(responseCode = "400", description = "Wrong target user's username")
    })
    public ResponseEntity<String> cancelFriendRequest(String receiverUsername) {
        return ResponseEntity.ok(communicationService.cancelFriendRequest(receiverUsername));
    }

    @PostMapping("deleteFriendship")
    @Operation(summary = "Deleting target user from friends")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Friend successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Wrong target user's username")
    })
    public ResponseEntity<String> deleteFriendship(String targetUsername) {
        return ResponseEntity.ok(communicationService.deleteFriendship(targetUsername));
    }

    @PostMapping("startChat")
    @Operation(summary = "Start char with target user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returned link to chat"),
            @ApiResponse(responseCode = "400", description = "Wrong target user's username"),
            @ApiResponse(responseCode = "409", description = "Not friends with target user")
    })
    public ResponseEntity<String> startChat(String username) {
        return ResponseEntity.ok(communicationService.startChat(username));
    }

    @ExceptionHandler(CommunicationService.FriendRequestAlreadyDeclinedException.class)
    public ResponseEntity<?> friendRequestAlreadyDeclined() {
        return new ResponseEntity<>("You already declined friend request from that user", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommunicationService.FriendRequestAlreadySentException.class)
    public ResponseEntity<?> friendRequestAlreadySent() {
        return new ResponseEntity<>("You already have friend request with that user", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FriendRequestService.FriendRequestNotFoundException.class)
    public ResponseEntity<?> friendRequestNotFound() {
        return new ResponseEntity<>("You didn't received friend request from that user", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommunicationService.NotFriendException.class)
    public ResponseEntity<?> notFriend() {
        return new ResponseEntity<>("This user is not your friend", HttpStatus.CONFLICT);
    }

}
