package com.example.api.services;

import com.example.api.models.Post;
import com.example.api.models.PostResponse;
import com.example.api.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityFeedService {
    private final PostService postService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public static class WrongPageNumberException extends RuntimeException { }

    private static final int PAGE_SIZE = 25;

    public List<PostResponse> getActivityFeed() {
        return getAllPosts().stream()
                .map(postService::postToPostResponse)
                .toList();
    }

    public List<PostResponse> getActivityFeedAtPage(int page) {
        if (page < 1)
            throw new WrongPageNumberException();
        return getAllPosts().stream()
                .skip((long) (page - 1) * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .map(postService::postToPostResponse)
                .toList();
    }

    public List<Post> getAllPosts() {
        // get current user
        User user = userService.getCurrentUser();

        // get user's publishers subscribed to
        List<User> subscribedTo = subscriptionService.getSubscribedTo(user);
        return postService.getAll(subscribedTo);
    }

    public List<PostResponse> getActivityFeedSortedByTime() {
        return getAllPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedOn))
                .map(postService::postToPostResponse)
                .toList();
    }

    public List<PostResponse> getActivityFeedSortedByTimeAtPage(int page) {
        if (page < 1)
            throw new WrongPageNumberException();
        return getAllPosts().stream()
                .sorted(Comparator.comparing(Post::getCreatedOn))
                .skip((long) (page - 1) * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .map(postService::postToPostResponse)
                .toList();
    }

}
