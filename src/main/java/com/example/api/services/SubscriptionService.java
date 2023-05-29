package com.example.api.services;

import com.example.api.models.Subscription;
import com.example.api.models.SubscriptionId;
import com.example.api.models.User;
import com.example.api.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription add(User subscriber, User publisher) {
        return subscriptionRepository.save(new Subscription(subscriber, publisher));
    }

    public void delete(User subscriber, User publisher) {
        subscriptionRepository.deleteById(new SubscriptionId(subscriber, publisher));
    }
}
