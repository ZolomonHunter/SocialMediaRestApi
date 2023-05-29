package com.example.api.repositories;

import com.example.api.models.FriendRequest;
import com.example.api.models.FriendRequestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestId> {

}
