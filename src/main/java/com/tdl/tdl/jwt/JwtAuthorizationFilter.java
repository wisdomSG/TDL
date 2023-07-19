package com.tdl.tdl.jwt;

import com.tdl.tdl.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.auth.Subject;
import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "Jwt 검증 필터")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    private final RedisUtil redisUtil;

    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RedisUtil redisUtil, RedisTemplate redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisUtil = redisUtil;
        this.redisTemplate = redisTemplate;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtUtil.refreshAccessToken(token);
                log.info("Token Error");
            }

            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        try {
            log.info("AuthFilter -> filterChain");
            filterChain.doFilter(request, response);
        } catch (FileUploadException e) {
            log.error(e.getMessage());
        }
    }

//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
//
//        String tokenValue = jwtUtil.resolveToken(req);
//
//
//        if (StringUtils.hasText(tokenValue)) { // 토큰값이 존재하는 경우 로직 수행
//            logger.info(redisUtil.getBlackList(tokenValue));
//
//            if (!jwtUtil.validateToken(tokenValue)) { // 토큰 유효성 검사
//                log.error("Token Error");
//                return;
//            }
//            // Redis에 해당 accessToken logout 여부를 확인
//            String isLogout = (String) redisTemplate.opsForValue().get(tokenValue); // token에 해당하는 값을 가져옴
//
//            if(ObjectUtils.isEmpty(isLogout)){
//
//                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
//
//                try {
//                    setAuthentication(info.getSubject());
//                } catch (Exception e) {
//                    log.error(e.getMessage());
//                    return;
//                }
//            }
//        }
//
//        filterChain.doFilter(req, res);
//    }


    // 인증 처리
    public void setAuthentication(String username) {

        logger.info(redisTemplate.opsForValue().getOperations().hasKey(username));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        // username -> user 조회 -> userDetails 에 담고 -> authentication의 principal 에 담고
        // -> securityContent 에 담고 -> SecurityContextHolder 에 담고
        // -> 이제 @AuthenticationPrincipal 로 조회할 수 있음
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
