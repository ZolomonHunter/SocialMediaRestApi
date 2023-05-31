package com.example.api.services;

import com.example.api.models.Friendship;
import com.example.api.models.FriendshipId;
import com.example.api.models.User;
import com.example.api.repositories.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public Friendship add(User first, User second) {
        Friendship result;
        if (first.getId().compareTo(second.getId()) < 0)
            result = new Friendship(first, second);
        else
            result = new Friendship(second, first);
        return friendshipRepository.save(result);
    }

    public void delete(User first, User second) {
        FriendshipId id;
        if (first.getId().compareTo(second.getId()) < 0)
            id = new FriendshipId(first, second);
        else
            id = new FriendshipId(second, first);
        friendshipRepository.deleteById(id);
    }

    public boolean isExist(User first, User second) {
        FriendshipId id;
        if (first.getId().compareTo(second.getId()) < 0)
            id = new FriendshipId(first, second);
        else
            id = new FriendshipId(second, first);
        return friendshipRepository.existsById(id);
    }
}
