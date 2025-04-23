package com.hong.bemajor.login;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    // 비밀키 (실제 프로젝트에서는 안전한 방법으로 보관해야 함)
        private String secretKey = "8Yf9bK6w5jD4h7T3pQ1xRoZyV7nS0cU3wN9eRzY1fGk=";

        // 토큰 생성
        public String generateToken(String username) {
            long expirationTime = 1000 * 60 * 60 * 24;  // 24시간 동안 유효
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 토큰 만료 시간
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }

        // 토큰에서 사용자명 추출
        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        // 토큰에서 클레임 추출
        public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
            Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        // 토큰에서 클레임을 추출
        private Claims extractAllClaims(String token) {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        }

        // 토큰 만료 여부 체크
        public boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        // 토큰에서 만료 시간 추출
        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        // 토큰 유효성 검증
        public boolean validateToken(String token, String username) {
            return (username.equals(extractUsername(token)) && !isTokenExpired(token));
        }
    // 7) 새로 추가: Jws<Claims> 반환용 (필터에서 바로 파싱)
        public Jws<Claims> validateToken(String token) throws JwtException {
            return Jwts.parser()
                       .setSigningKey(secretKey)
                       .parseClaimsJws(token);
        }
}
