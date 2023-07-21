package com.tdl.tdl.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdl.tdl.dto.*;

import com.tdl.tdl.jwt.JwtUtil;
import com.tdl.tdl.service.KakaoService;
import com.tdl.tdl.service.UserService;
import jakarta.servlet.http.Cookie;
import com.tdl.tdl.security.UserDetailsImpl;
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
    private final KakaoService kakaoService;

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
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        try {
            userService.logout(request, userDetails.getUser());
            return ResponseEntity.ok().body("로그아웃 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 제한된 사용자입니다.");
        }
    }
    // 유저 검색 기능
    @GetMapping("/user/search")
    public UserSearchResponseDto SearchUser(@RequestParam("keyword") UserSearchRequestDto dto) {
        return userService.searchUser(dto);
    }

    @GetMapping("/user/login")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponseDto> handleException(IllegalArgumentException ex) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
    }
}
