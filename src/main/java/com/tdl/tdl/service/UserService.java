package com.tdl.tdl.service;


import com.tdl.tdl.dto.SignupRequestDto;
import com.tdl.tdl.dto.UserRequestDto;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.entity.UserRoleEnum;
import com.tdl.tdl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_TOKEN = "123KIM"; // ADMIN_TOKEN: 일반사용자인지 관리자인지 구분하기위해서


    // 회원가입 메서드
    public void signup(SignupRequestDto dto) {
        String email = dto.getEmail();
        String password = passwordEncoder.encode(dto.getPassword());
        String username = dto.getUsername();

        // 회원 중복 확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
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

        User user = new User(email, password, username, role);
        userRepository.save(user);

    }

    // 로그인 메서드
}
