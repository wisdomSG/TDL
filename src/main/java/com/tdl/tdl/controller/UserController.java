package com.tdl.tdl.controller;


import com.tdl.tdl.dto.SignupRequestDto;
import com.tdl.tdl.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
