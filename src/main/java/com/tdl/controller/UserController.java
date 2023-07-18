package com.tdl.controller;


import com.tdl.service.UserService;
import com.tdl.dto.ApiResponseDto;
import com.tdl.dto.SignupRequestDto;
import com.tdl.dto.UserRequestDto;
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

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponseDto> handleException(IllegalArgumentException ex) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.BAD_REQUEST);
    }
}
