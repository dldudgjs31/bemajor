package com.hong.bemajor.login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 토큰 기반 인증 필터
 * - 모든 요청마다 JWT 유효성을 검사하고, 유효한 경우 SecurityContext에 인증 정보 설정
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService uds;

    /**
     * 생성자 주입
     * @param jwtUtil JWT 토큰 생성 및 검증 유틸
     * @param uds    Spring Security UserDetailsService
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    /**
     * HTTP 요청별 필터 로직
     * 1. Authorization 헤더에서 Bearer 토큰 추출
     * 2. 토큰 검증 실패 시 401 응답
     * 3. 검증 성공 시 UserDetails 조회 후 인증 컨텍스트에 설정
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
                                    throws ServletException, IOException {
        // Authorization 헤더 확인
        String header = req.getHeader("Authorization");
        // Bearer 토큰 존재 여부 확인
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);  // "Bearer " 접두사 제거
            try {
                // 토큰 유효성 검사 및 Claims 추출
                Jws<Claims> jws = jwtUtil.validateToken(token);
                String username = jws.getBody().getSubject();  // 페이로드의 subject에서 사용자명 획득

                // 인증 정보가 없고 username이 유효한 경우
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 사용자 정보 로드
                    UserDetails user = uds.loadUserByUsername(username);
                    // 인증 토큰 생성 (패스워드 검증은 JwtUtil에서 수행)
                    Authentication auth =
                      new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    // SecurityContext에 인증 정보 저장
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException ex) {
                // 토큰 검증 실패 시 401 Unauthorized 응답
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;  // 이후 필터 체인 진행 중단
            }
        }
        // 필터 체인 다음 단계 실행
        chain.doFilter(req, res);
    }
}
