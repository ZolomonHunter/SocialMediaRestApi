package com.example.api.repositories;

import com.example.api.models.Friendship;
import com.example.api.models.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {
}
