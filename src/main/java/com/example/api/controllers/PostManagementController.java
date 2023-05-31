package com.example.api.controllers;

import com.example.api.services.PostManagementService;
import com.example.api.models.PostResponse;
import com.example.api.services.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JwtAuth")
public class PostManagementController {
    private final PostManagementService postManagementService;

    @PostMapping(value = "createPost", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostResponse> createPost(String header,
                                                   Optional<String> description,
                                                   @ModelAttribute Optional<MultipartFile> file) {
        return ResponseEntity.ok(postManagementService.createPost(header, description, file));
    }

    @GetMapping(value = "getMyPosts")
    public ResponseEntity<List<PostResponse>> getMyPosts() {
        return ResponseEntity.ok(postManagementService.getMyPosts());
    }

    @PostMapping(value = "updatePost", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostResponse> updatePost(int id,
                                                     Optional<String> header,
                                                     Optional<String> description,
                                                     @ModelAttribute Optional<MultipartFile> file) {
        return ResponseEntity.ok(postManagementService.updatePost(id, header, description, file));
    }

    @GetMapping(value = "getPosts")
    public ResponseEntity<List<PostResponse>> getPosts(String username) {
        return ResponseEntity.ok(postManagementService.getPosts(username));
    }

    @ExceptionHandler(PostManagementService.MissingPostHeaderException.class)
    public ResponseEntity<?> missingPostHeader() {
        return new ResponseEntity<>("Post's header can't be empty", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostManagementService.ImageLoadException.class)
    public ResponseEntity<?> imageLoadException() {
        return new ResponseEntity<>("Can't load image to server", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PostManagementService.UnsupportedContentTypeException.class)
    public ResponseEntity<?> unsupportedContentType() {
        return new ResponseEntity<>("Image must be of type \"image/*\"", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostManagementService.FilenameAlreadyUsedException.class)
    public ResponseEntity<?> filenameAlreadyUsed() {
        return new ResponseEntity<>("Image with same name already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PostService.PostNotFoundException.class)
    public ResponseEntity<?> postNotFound() {
        return new ResponseEntity<>("No posts with such id", HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(PostManagementService.NotUsersPostException.class)
    public ResponseEntity<?> notUsersPost() {
        return new ResponseEntity<>("This is not your post", HttpStatus.FORBIDDEN);
    }
}
