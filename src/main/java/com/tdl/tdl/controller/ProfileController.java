package com.tdl.tdl.controller;

import com.tdl.tdl.dto.CheckRequestDto;
import org.springframework.ui.Model;
import com.tdl.tdl.dto.UserProfileRequestDto;
import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.UserService;
import com.tdl.tdl.dto.ApiResponseDto;
import com.tdl.tdl.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ApiResponseDto> updateProfile(@PathVariable Long userId, @RequestPart("userInfo") UserProfileRequestDto userProfileRequestDto,
                                                        @RequestPart("imgUrl") MultipartFile multipartFile) {
        return userService.updateProfile(userId, multipartFile, userProfileRequestDto);
    }

    @PostMapping("/check")
    public ResponseEntity<String> passwordCheck(@RequestBody CheckRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.passwordCheck(requestDto, userDetails.getUser());
    }
}