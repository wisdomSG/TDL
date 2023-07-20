package com.tdl.tdl.controller;

import com.tdl.tdl.dto.UserProfileRequestDto;
import com.tdl.tdl.service.UserService;
import com.tdl.tdl.dto.ApiResponseDto;
import com.tdl.tdl.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
}