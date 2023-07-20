package com.tdl.tdl.jwt;

import com.tdl.tdl.dto.TokenDto;
import com.tdl.tdl.dto.UserResponseDto;
import com.tdl.tdl.entity.User;
import com.tdl.tdl.entity.UserRoleEnum;
import com.tdl.tdl.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Value("${jwt.secret.key}") // Base64 Encoded SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final RedisUtil redisUtil;

    private final UserRepository userRepository;

    public JwtUtil(RedisUtil redisUtil, UserRepository userRepository) {
        this.redisUtil = redisUtil;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // token 이 null 또는 공백인지 체크 && 토큰이 정상적으로 Bearer 를 가지고 있는지 체크
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return  BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();

    }

    public String createRefreshToken() {
        Date date = new Date();
        return Jwts.builder()
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String refreshAccessToken(String accessToken) {
        // Access Token이 만료되었는지 검증
        if (isAccessTokenExpired(accessToken)) {
            // Access Token에서 사용자 정보 추출
            Claims claims = getUserInfoFromToken(accessToken);
            String username = claims.getSubject();

            // Redis에서 저장된 Refresh Token 가져오기
            String refreshToken = (String) redisUtil.get(username);

            // Redis에 저장된 Refresh Token이 유효하고 일치하는지 확인
            if (StringUtils.hasText(refreshToken) && refreshToken.equals(claims.get("refreshToken"))) {
                // 사용자 정보를 기반으로 새로운 Access Token 생성
                User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException());
                String newAccessToken = createToken(username, user.getRole());

                // 새로운 Access Token 반환
                return newAccessToken;
            }
        }

        return null;
    }

    private boolean isAccessTokenExpired(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
            if (redisUtil.hasKeyBlackList(accessToken)) {
                throw new RuntimeException("로그아웃 !!");
            }
            return true;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }


    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            if (redisUtil.hasKeyBlackList(token)) {
                throw new RuntimeException("로그아웃을 했습니다 !");
            }
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    // 남은 유효 시간 계산
    public Long getExpriration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 토큰으로부터 유저 정보를 받아옴
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 사용한 암호화 키를 설정합니다.
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 유저 정보를 가져옵니다.
        String username = claims.getSubject(); // 사용자 식별자값(ID)
        Collection<String> authorities = (Collection<String>) claims.get("AUTHORIZATION_KEY"); // 사용자 권한

        // 권한 정보를 SimpleGrantedAuthority로 변환합니다.
        Collection<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UsernamePasswordAuthenticationToken 객체를 생성하여 반환합니다.
        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
    }
}

