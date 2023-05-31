package com.example.api.services;

import com.example.api.models.Post;
import com.example.api.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
