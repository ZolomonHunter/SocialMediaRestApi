package com.example.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(FriendRequestId.class)
public class FriendRequest {
    @Id
    @ManyToOne
    private User sender;
    @Id
    @ManyToOne
    private User receiver;
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;
}
