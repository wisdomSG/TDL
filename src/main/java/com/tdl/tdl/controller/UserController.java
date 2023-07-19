package com.tdl.tdl.controller;


import com.tdl.tdl.dto.*;
import com.tdl.tdl.security.UserDetailsImpl;
import com.tdl.tdl.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tdl")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입 기능
    @PostMapping("/user/signup")
    public ResponseEntity<String> Signup(@Valid @RequestBody SignupRequestDto dto) {
        userService.signup(dto);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    // 로그인 기능
    @PostMapping("/user/login")
    public TokenDto Login(@RequestBody UserRequestDto dto, HttpServletResponse response) {
        return userService.login(dto, response);

    }

    // 로그아웃 기능
    @PostMapping("/logoout")
    public ResponseEntity<String> logout (HttpServletRequest request) {
        userService.logout(request);
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    // 유저 검색 기능
    @GetMapping("/user/search")
    public UserSearchResponseDto SearchUser(@RequestParam("keyword") UserSearchRequestDto dto) {
        return userService.searchUser(dto);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponseDto> handleException(IllegalArgumentException ex) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
    }
}
