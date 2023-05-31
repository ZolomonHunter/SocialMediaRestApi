package com.example.api.services;

import com.example.api.models.Post;
import com.example.api.models.PostResponse;
import com.example.api.models.User;
import com.example.api.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public static class PostNotFoundException extends RuntimeException {
    }

    public Post update(Post post) {
        return postRepository.save(post);
    }

    public Post add(Post post) {
        return postRepository.save(post);
    }

    public Post get(int id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    public void delete(int id) {
        postRepository.deleteById(id);
    }

    public List<Post> getAll(List<User> users) {
        List<Post> result = new ArrayList<>();
        users.forEach(user -> result.addAll(user.getPosts()));
        return result;
    }

    public PostResponse postToPostResponse(Post post) {
        return new PostResponse(post.getId(), post.getHeader(), post.getDescription(), post.getImageUrl());
    }
}
