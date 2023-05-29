package com.example.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(FriendshipId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {
    @ManyToOne
    @Id
    private User firstUser;
    @ManyToOne
    @Id
    private User secondUser;
}
