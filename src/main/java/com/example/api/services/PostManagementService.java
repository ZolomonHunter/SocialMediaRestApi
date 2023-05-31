package com.example.api.services;

import com.example.api.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostManagementService {
    private final PostService postService;
    private final UserService userService;
    private final String downloadDirectory = System.getProperty("user.dir") + "/images";


    public static class MissingPostHeaderException extends RuntimeException { }
    public static class UnsupportedContentTypeException extends RuntimeException { }
    public static class ImageLoadException extends RuntimeException { }
    public static class FilenameAlreadyUsedException extends RuntimeException { }
    public static class NotUsersPostException extends RuntimeException { }

    public PostResponse createPost(String header, Optional<String> description, Optional<MultipartFile> file) {
        // post must contain at least header
        if (header == null || header.isBlank())
            throw new MissingPostHeaderException();

        // validate image
        validateImage(file);

        // make path for file
        User user = userService.getCurrentUser();

        // loading image if present
        String imagePath = loadImageToDb(file, user);

        // adding to db
        Post post = postService.add(
                Post.builder()
                        .header(header)
                        .description(description.orElse(""))
                        .imageUrl(imagePath)
                        .owner(user)
                .build());
        return postService.postToPostResponse(post);
    }

    public PostResponse updatePost(int id, Optional<String> header, Optional<String> description, Optional<MultipartFile> file) {
        // get post
        Post post = postService.get(id);

        // check if user is post owner
        User user = userService.getCurrentUser();
        if (post.getOwner() != user)
            throw new NotUsersPostException();

        // change new properties
        header.ifPresent(post::setHeader);
        description.ifPresent(post::setDescription);
        if (file.isPresent())
            post.setImageUrl(replaceImage(file, post));

        postService.update(post);
        return postService.postToPostResponse(post);
    }

    private String deletePost(int id) {
        // get post
        Post post = postService.get(id);

        // check if user is post owner
        User user = userService.getCurrentUser();
        if (post.getOwner() != user)
            throw new NotUsersPostException();

        deleteImage(Path.of(post.getImageUrl()));
        postService.delete(id);
        return "Post deleted";
    }

    private void validateImage(Optional<MultipartFile> file) {
        if (file.isPresent() &&
                (file.get().getContentType() == null ||
                        !file.get().getContentType().startsWith("image")))
            throw new UnsupportedContentTypeException();
    }

    private String loadImageToDb(Optional<MultipartFile> file, User user) {
        Path folderPath = Path.of(downloadDirectory, user.getUsername());
        Path imagePath = Path.of("");
        if (file.isPresent()) {
            MultipartFile image = file.get();
            try {
                // creating dir for image
                if (!Files.exists(folderPath))
                    Files.createDirectories(folderPath);
                imagePath = Path.of(folderPath.toString(), image.getOriginalFilename());
                // copying
                Files.copy(image.getInputStream(), imagePath);
            } catch (FileAlreadyExistsException e) {
                throw new FilenameAlreadyUsedException();
            } catch (IOException e) {
                throw new ImageLoadException();
            }
        }
        return imagePath.toString();
    }


    private String replaceImage(Optional<MultipartFile> file, Post post) {
        validateImage(file);
        String oldImage = post.getImageUrl();
        String newImage = loadImageToDb(file, post.getOwner());
        if (oldImage.isBlank())
            return newImage;
        Path oldImagePath = Path.of(oldImage);
        deleteImage(oldImagePath);
        return newImage;
    }

    private void deleteImage(Path imagePath) {
        if (imagePath.toFile().exists()) {
            try {
                Files.delete(imagePath);
            } catch (IOException e) {
                throw new ImageLoadException();
            }
        }
    }

    public List<PostResponse> getMyPosts() {
        // get user
        User user = userService.getCurrentUser();

        return user.getPosts().stream().map(postService::postToPostResponse).toList();
    }

    public List<PostResponse> getPosts(String username) {
        // get user
        User user = userService.get(username);

        return user.getPosts().stream().map(postService::postToPostResponse).toList();
    }

}
