package com.example.api.controllers;

import com.example.api.services.PostManagementService;
import com.example.api.models.PostResponse;
import com.example.api.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Creating post with header, description and image")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returned created post",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Header is missing or image is not \"image/*\""),
            @ApiResponse(responseCode = "409", description = "Already uploaded image with the same name"),
            @ApiResponse(responseCode = "500", description = "Server error while loading image")
    })
    public ResponseEntity<PostResponse> createPost(String header,
                                                   Optional<String> description,
                                                   @ModelAttribute Optional<MultipartFile> file) {
        return ResponseEntity.ok(postManagementService.createPost(header, description, file));
    }

    @GetMapping(value = "getMyPosts")
    @Operation(summary = "Getting all created posts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returned created posts",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)) })
    })
    public ResponseEntity<List<PostResponse>> getMyPosts() {
        return ResponseEntity.ok(postManagementService.getMyPosts());
    }

    @PostMapping(value = "updatePost", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Updating post info")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returned updated post",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Image is not \"image/*\""),
            @ApiResponse(responseCode = "403", description = "Not authenticated user's post"),
            @ApiResponse(responseCode = "404", description = "Post with target id not found"),
            @ApiResponse(responseCode = "409", description = "Already uploaded image with the same name"),
            @ApiResponse(responseCode = "500", description = "Server error while loading image")
    })
    public ResponseEntity<PostResponse> updatePost(int id,
                                                     Optional<String> header,
                                                     Optional<String> description,
                                                     @ModelAttribute Optional<MultipartFile> file) {
        return ResponseEntity.ok(postManagementService.updatePost(id, header, description, file));
    }

    @GetMapping(value = "getPosts")
    @Operation(summary = "Get all posts of target user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returned list of user's post",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Wrong target user's username")
    })
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
        return new ResponseEntity<>("No posts with such id", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(PostManagementService.NotUsersPostException.class)
    public ResponseEntity<?> notUsersPost() {
        return new ResponseEntity<>("This is not your post", HttpStatus.FORBIDDEN);
    }
}
