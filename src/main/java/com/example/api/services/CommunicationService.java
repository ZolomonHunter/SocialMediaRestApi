package com.example.api.services;

import com.example.api.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunicationService {
    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final FriendshipService friendshipService;
    private final SubscriptionService subscriptionService;

    public static class FriendRequestAlreadySent extends RuntimeException { }
    public static class FriendRequestAlreadyDeclined extends RuntimeException { }


    public String declineFriendRequest(String senderUsername) {
        // get sender and receiver
        User sender = userService.getUser(senderUsername);
        User receiver = userService.getCurrentUser();

        // get request
        FriendRequest request = friendRequestService.get(sender, receiver);

        // set request status to declined
        if (request.getStatus() == FriendRequestStatus.DECLINED)
            throw new FriendRequestAlreadyDeclined();
        request.setStatus(FriendRequestStatus.DECLINED);
        friendRequestService.update(request);
        return "You declined friend request";
    }

    public List<FriendRequestResponse> getSentFriendRequests() {
        // get all sent requests
        List<FriendRequest> friendRequests = friendRequestService.getAllSent();
        // return user-friendly response
        return friendRequestService.friendRequestListResponse(friendRequests);
    }

    public List<FriendRequestResponse> getReceivedFriendRequests() {
        // get all received requests
        List<FriendRequest> friendRequests = friendRequestService.getAllReceived();
        // filter by pending
        friendRequests = friendRequestService.getPending(friendRequests);
        // return user-friendly response
        return friendRequestService.friendRequestListResponse(friendRequests);
    }


    public String sendFriendRequest(String receiverUsername) {
        // get sender and receiver
        User sender = userService.getCurrentUser();
        User receiver = userService.getUser(receiverUsername);

        // check if user already sent request
        if (friendRequestService.isExist(sender, receiver))
            throw new FriendRequestAlreadySent();

        // add new friend request
        friendRequestService.add(sender, receiver);
        // subscribe
        subscriptionService.add(sender, receiver);
        return "New friend request sent";
    }

    public String acceptFriendRequest(String senderUsername) {
        // get sender and receiver
        User sender = userService.getUser(senderUsername);
        User receiver = userService.getCurrentUser();

        // get request
        FriendRequest request = friendRequestService.get(sender, receiver);

        // accept request and add to friends
        friendRequestService.delete(request);
        friendshipService.add(sender, receiver);

        // subscribe
        subscriptionService.add(receiver, sender);
        return "You are now friends";
    }

    public String cancelFriendRequest(String receiverUsername) {
        // get sender and receiver
        User sender = userService.getCurrentUser();
        User receiver = userService.getUser(receiverUsername);

        // get request
        FriendRequest request = friendRequestService.get(sender, receiver);

        // delete request
        friendRequestService.delete(request);

        // unsubscribe
        subscriptionService.delete(sender, receiver);
        return "You cancelled friend request";
    }

    public String deleteFriendship(String username) {
        // get target and current users
        User target = userService.getUser(username);
        User current = userService.getCurrentUser();

        // users are not friends anymore
        friendshipService.delete(current, target);
        // target user is now pending friend request
        friendRequestService.add(target, current);

        // unsubscribe current user from target
        subscriptionService.delete(current, target);
        return "You are no longer friends";
    }
}
