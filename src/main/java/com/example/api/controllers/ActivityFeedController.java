package com.example.api.controllers;

import com.example.api.models.PostResponse;
import com.example.api.services.ActivityFeedService;
import com.example.api.services.ValidationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JwtAuth")
public class ActivityFeedController {
    private final ActivityFeedService activityFeedService;
    
    @GetMapping("getActivityFeed")
    @Operation(summary = "Get authenticated user's activity feed")
    @ApiResponse(responseCode = "200", description = "Returns list of posts",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PostResponse.class)) })
    public ResponseEntity<List<PostResponse>> getActivityFeed() {
        return ResponseEntity.ok(activityFeedService.getActivityFeed());
    }



    @GetMapping("getActivityFeedSortedByTime")
    @Operation(summary = "Get authenticated user's activity feed, sorted by time")
    @ApiResponse(responseCode = "200", description = "Returns list of posts",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PostResponse.class)) })
    public ResponseEntity<List<PostResponse>> getActivityFeedSortedByTime() {
        return ResponseEntity.ok(activityFeedService.getActivityFeedSortedByTime());
    }

    @GetMapping("getActivityFeedSortedByTimeAtPage")
    @Operation(summary = "Get authenticated user's activity feed, supporting pagination, posts sorted by time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of posts",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Wrong page number",
                    content = @Content)})
    public ResponseEntity<List<PostResponse>> getActivityFeedSortedByTimeAtPage(int page) {
        return ResponseEntity.ok(activityFeedService.getActivityFeedSortedByTimeAtPage(page));
    }

    @GetMapping("getActivityFeedAtPage")
    @Operation(summary = "Get authenticated user's activity feed, supporting pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns list of posts",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Wrong page number",
                    content = @Content)})
    public ResponseEntity<List<PostResponse>> getActivityFeedAtPage(int page) {
        return ResponseEntity.ok(activityFeedService.getActivityFeedAtPage(page));
    }

    @ExceptionHandler(ActivityFeedService.WrongPageNumberException.class)
    public ResponseEntity<?> wrongPageNumber() {
        return new ResponseEntity<>("Page number must be > 0", HttpStatus.BAD_REQUEST);
    }
}
