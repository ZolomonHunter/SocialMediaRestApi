package com.example.api.models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestResponse {
    private String senderUsername;
    private String receiverUsername;
    private String status;

    public FriendRequestResponse (FriendRequest request) {
        senderUsername = request.getSender().getUsername();
        receiverUsername = request.getReceiver().getUsername();
        status = request.getStatus().toString();
    }
}
