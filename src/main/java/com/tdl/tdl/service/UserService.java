package com.tdl.tdl.service;


import com.tdl.tdl.dto.*;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.entity.UserRoleEnum;
import com.tdl.tdl.jwt.JwtUtil;
import com.tdl.tdl.jwt.RedisUtil;
import com.tdl.tdl.repository.UserRepository;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisUtil redisUtil;

    private final String ADMIN_TOKEN = "123KIM"; // ADMIN_TOKEN: 일반사용자인지 관리자인지 구분하기위해서


    // 회원가입 메서드
    public void signup(SignupRequestDto dto) {
        String username = dto.getUsername();
        String password = passwordEncoder.encode(dto.getPassword());
        String profilename = dto.getProfilename();

        // 회원 중복 확인
        Optional<User> checkEmail = userRepository.findByUsername(username);
        if(checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 email이 존재합니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;

        if(!dto.getAdminToken().isEmpty()) { // adminToken을 입력했다면
            dto.setAdmin(true); // Admin = true;
        }

        if (dto.isAdmin()) { // Admin이 true 이면
            if(!ADMIN_TOKEN.equals(dto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, profilename, role);
        userRepository.save(user);

    }


    @Transactional
    public TokenDto login(UserRequestDto dto, HttpServletResponse response) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = findUser(username);

        // Password verification
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 헤더에 추가
        String token = jwtUtil.createToken(username, user.getRole()); // JWT 생성
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); // 헤더에 추가

        // RefreshToken 생성 및 Redis에 저장
        String refreshToken = jwtUtil.createRefreshToken(); // RefreshToken 생성

        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(username, refreshToken);

        return new TokenDto(token, refreshToken);
    }


    @Transactional
    public void logout(HttpServletRequest request, User user) {
        String accessToken = jwtUtil.resolveToken(request);
        String username = user.getUsername();

//        // 사용자가 Redis 블랙리스트에 있는지 확인
//        if (redisTemplate.opsForValue().getOperations().hasKey(username)) {
//            throw new IllegalArgumentException("로그인이 제한된 사용자입니다.");
//        }


        if(!redisTemplate.hasKey(username)) { // username으로 Redis에서 value(RefreshToken)가 존재하는지 확인
            throw new IllegalArgumentException("username에 관하여 refreshToken이 없습니다.");
        }
        // Redis에서 RefreshToken 삭제
        redisUtil.delete(username);

        // Access Token 유효시간 가져와서 BlackList에 저장
        Long expiration = jwtUtil.getExpriration(accessToken);
        redisUtil.setBlackList(accessToken, "logout", expiration.intValue());

    }

    public UserResponseDto lookupProfile(Long userId) {
        User user = findUserProfile(userId);
        return new UserResponseDto(user);
    }

    @Transactional
    public ResponseEntity<ApiResponseDto> updateProfile(Long userId, UserProfileRequestDto requestDto) {
        Optional<User> inputUpdateUser = userRepository.findById(userId);

        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = inputUpdateUser.get();
        user.update(requestDto, password);

        // User -> UserResponseDto
        UserResponseDto userResponseDto = new UserResponseDto(user);

        ApiResponseDto result = new ApiResponseDto(HttpStatus.OK.value(), "프로필 수정 성공", userResponseDto);

        return ResponseEntity.status(200).body(result);
    }

    public UserSearchResponseDto searchUser(UserSearchRequestDto dto) {

        List<User> user = userRepository.findByUsernameContaining(dto.getKeyword());

        return new UserSearchResponseDto(user);

    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    private User findUserProfile(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }



}
