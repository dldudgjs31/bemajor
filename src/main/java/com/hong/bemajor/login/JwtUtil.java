package com.hong.bemajor.login;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 토큰 생성 및 검증 유틸리티 클래스
 * - 비밀키를 사용해 토큰을 서명(Sign)하고, 클레임(Claims)을 파싱
 * - 실제 프로젝트에서는 비밀키를 안전한 저장소(예: 환경 변수, Vault)에서 관리해야 함
 */
@Component
public class JwtUtil {
    /**
     * 서명(Signature)에 사용할 비밀키 (Base64 인코딩)
     */
    private String secretKey = "8Yf9bK6w5jD4h7T3pQ1xRoZyV7nS0cU3wN9eRzY1fGk=";

    /**
     * JWT 토큰 생성
     * @param username 토큰의 subject(주체)로 사용할 사용자명
     * @return 서명된 JWT 토큰 문자열
     */
    public String generateToken(String username) {
        long expirationTime = 1000 * 60 * 60 * 24;  // 24시간 유효
        return Jwts.builder()
                .setSubject(username)                        // 사용자명을 subject로 설정
                .setIssuedAt(new Date())                     // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // HS256 알고리즘, 비밀키로 서명
                .compact();                                   // 토큰 생성
    }

    /**
     * 토큰에서 사용자명(subject) 추출
     * @param token JWT 토큰 문자열
     * @return 토큰에 설정된 subject
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 토큰에서 특정 클레임 값 추출
     * @param token JWT 토큰 문자열
     * @param claimsResolver Claims에서 추출할 값 지정 함수
     * @param <T> 클레임 값 타입
     * @return resolve 함수가 반환하는 클레임 값
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 토큰에서 모든 클레임(Claims) 파싱
     * @param token JWT 토큰 문자열
     * @return 파싱된 Claims 객체
     */
    private Claims extractAllClaims(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(secretKey)        // 서명 검증에 사용할 비밀키 설정
                .parseClaimsJws(token)           // JWS 파싱 및 서명 검증
                .getBody();                      // 클레임 반환
    }

    /**
     * 토큰 만료 여부 확인
     * @param token JWT 토큰 문자열
     * @return 만료되었으면 true, 유효하면 false
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 토큰에서 만료 시간 추출
     * @param token JWT 토큰 문자열
     * @return 토큰에 설정된 만료 일시
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 토큰 유효성 검증 (사용자명 비교 및 만료 여부 확인)
     * @param token JWT 토큰 문자열
     * @param username 검증할 사용자명
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    /**
     * JWT 파싱 결과인 Jws<Claims> 반환 (필터에서 바로 사용)
     * @param token JWT 토큰 문자열
     * @return 파싱된 JWS 객체
     * @throws JwtException 파싱 또는 검증 실패 시 예외 발생
     */
    public Jws<Claims> validateToken(String token) throws JwtException {
        return Jwts.parser()
                   .setSigningKey(secretKey)
                   .parseClaimsJws(token);  // 서명 검증 후 JWS 반환
    }
}
