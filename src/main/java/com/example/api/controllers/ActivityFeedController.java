package com.example.api.controllers;

import com.example.api.models.PostResponse;
import com.example.api.services.ActivityFeedService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@SecurityRequirement(name = "JwtAuth")
public class ActivityFeedController {
    private final ActivityFeedService activityFeedService;


    @GetMapping("getActivityFeed")
    public ResponseEntity<List<PostResponse>> getActivityFeed() {
        return ResponseEntity.ok(activityFeedService.getActivityFeed());
    }

    @GetMapping("getActivityFeedSortedByTime")
    public ResponseEntity<List<PostResponse>> getActivityFeedSortedByTime() {
        return ResponseEntity.ok(activityFeedService.getActivityFeedSortedByTime());
    }

    @GetMapping("getActivityFeedSortedByTimeAtPage")
    public ResponseEntity<List<PostResponse>> getActivityFeedSortedByTimeAtPage(int page) {
        return ResponseEntity.ok(activityFeedService.getActivityFeedSortedByTimeAtPage(page));
    }

    @GetMapping("getActivityFeedAtPage")
    public ResponseEntity<List<PostResponse>> getActivityFeedAtPage(int page) {
        return ResponseEntity.ok(activityFeedService.getActivityFeedAtPage(page));
    }
}
