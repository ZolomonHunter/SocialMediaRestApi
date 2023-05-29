package com.example.api.services;

import com.example.api.models.*;
import com.example.api.repositories.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    public static class FriendRequestNotFound extends RuntimeException { }

    public FriendRequest update(FriendRequest request) {
        return friendRequestRepository.save(request);
    }

    public void delete(FriendRequest request) {
        friendRequestRepository.delete(request);
    }

    public FriendRequest get(User sender, User receiver) {
        return friendRequestRepository.findById(new FriendRequestId(sender, receiver))
                .orElseThrow(FriendRequestNotFound::new);
    }

    public FriendRequest add(User sender, User receiver) {
        return friendRequestRepository.save(new FriendRequest(sender, receiver, FriendRequestStatus.PENDING));
    }

    public boolean isExist(User sender, User receiver) {
        return friendRequestRepository.existsById(new FriendRequestId(sender, receiver)) ||
                friendRequestRepository.existsById(new FriendRequestId(receiver, sender));
    }

    private List<FriendRequest> getAllBy(Predicate<FriendRequest> filter) {
        return friendRequestRepository
                .findAll()
                .stream()
                .filter(filter)
                .toList();
    }

    public List<FriendRequestResponse> friendRequestListResponse(List<FriendRequest> requests) {
        List<FriendRequestResponse> result = new ArrayList<>();
        for (FriendRequest request : requests)
            result.add(new FriendRequestResponse(request));
        return result;
    }

    public List<FriendRequest> getPending(List<FriendRequest> requests) {
        return requests
                .stream()
                .filter(
                        request -> request.getStatus() == FriendRequestStatus.PENDING
                )
                .toList();
    }

    public List<FriendRequest> getAllReceived() {
        User receiver = userService.getCurrentUser();
        return receiver.getReceivedFriendRequests();
    }

    public List<FriendRequest> getAllSent() {
        User sender = userService.getCurrentUser();
        return sender.getSentFriendRequests();
    }
}
