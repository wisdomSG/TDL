package com.tdl.controller;

import com.tdl.dto.UserProfileRequestDto;
import com.tdl.service.UserService;
import com.tdl.dto.ApiResponseDto;
import com.tdl.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tdl/user/profile")
public class ProfileController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseBody
    public UserResponseDto lookupProfile(@PathVariable Long userId) {
        return userService.lookupProfile(userId);
    }

    @PutMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> updateProfile(@PathVariable Long userId, @RequestBody UserProfileRequestDto userProfileRequestDto) {
        return userService.updateProfile(userId, userProfileRequestDto);
    }
}
