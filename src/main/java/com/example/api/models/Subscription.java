package com.example.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@IdClass(SubscriptionId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    private User subscriber;
    @Id
    private User publisher;
}
