package com.tdl.tdl.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdl.tdl.dto.*;
import com.tdl.tdl.jwt.JwtUtil;
import com.tdl.tdl.service.KakaoService;
import com.tdl.tdl.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> Login(@RequestBody UserRequestDto dto, HttpServletResponse response) {
        userService.login(dto, response);
        return ResponseEntity.ok().body("로그인 성공 ");
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
