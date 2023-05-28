package com.example.api.services;

import com.example.api.models.FriendRequest;
import com.example.api.models.FriendRequestId;
import com.example.api.models.User;
import com.example.api.repositories.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequest addFriendRequest(FriendRequest request) {
        return friendRequestRepository.save(request);
    }

    public boolean isExist(User sender, User receiver) {
        return friendRequestRepository.existsById(new FriendRequestId(sender, receiver));
    }
}
