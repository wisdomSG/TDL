package com.tdl.tdl.service;


import com.tdl.tdl.dto.SignupRequestDto;
import com.tdl.tdl.dto.UserRequestDto;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.entity.UserRoleEnum;
import com.tdl.tdl.jwt.JwtUtil;
import com.tdl.tdl.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

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

    // 로그인 메서드
    public void login(UserRequestDto dto, HttpServletResponse response) {
        String username = dto.getUsername();
        String password = dto.getPassword();


        User user = findUser(username);

        // password 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 헤더에 추가
        String token = jwtUtil.createToken(username, user.getRole()); // JWT 생성
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); // 헤더에 추가

    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }


}
