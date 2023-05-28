package com.example.api.services;

import com.example.api.models.FriendRequest;
import com.example.api.models.FriendRequestStatus;
import com.example.api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunicationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final FriendRequestService friendRequestService;

    public static class FriendRequestAlreadySent extends RuntimeException { }

    public String sendFriendRequest(String receiverUsername) {
        // get sender and receiver
        String senderUsername = jwtService.getCurrentUsername();
        User sender = userService.getUser(senderUsername);
        User receiver = userService.getUser(receiverUsername);

        // check if user already sent request
        if (friendRequestService.isExist(sender, receiver))
            throw new FriendRequestAlreadySent();

        // add new friend request
        friendRequestService.addFriendRequest(new FriendRequest(sender, receiver, FriendRequestStatus.PENDING));
        return "New friend request sent";
    }
}
