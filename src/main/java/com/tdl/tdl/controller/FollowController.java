package com.tdl.tdl.controller;

import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tdl/user")
@RequiredArgsConstructor
public class FollowController {
    private final UserService userService;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.followUser(userId, userDetails);
    }

    @DeleteMapping("/{userId}/unfollow")
    public void unfollowUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.unfollowUser(userId, userDetails);
    }
}
