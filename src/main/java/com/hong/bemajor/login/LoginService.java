package com.hong.bemajor.login;

import com.hong.bemajor.users.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * 로그인 서비스
 * - 스프링 시큐리티 AuthenticationManager를 통해 사용자 인증 수행
 * - 인증 성공 시 JWT 토큰 발급
 */
@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersDao usersDao;

    /**
     * 생성자 주입
     * @param authenticationManager Spring Security 인증 매니저
     * @param jwtUtil JWT 토큰 생성/검증 유틸
     * @param usersDao 사용자 정보 조회 DAO (필요 시 사용)
     */
    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsersDao usersDao) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usersDao = usersDao;
    }

    /**
     * 로그인 처리 메서드
     * @param loginRequest 클라이언트로부터 전달받은 로그인 요청 데이터 (login_id, password)
     * @return 발급된 JWT 토큰 문자열
     * @throws AuthenticationException 인증 실패 시 발생
     */
    public String authenticate(LoginRequest loginRequest) throws AuthenticationException {

        // 1. UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getLogin_id(), loginRequest.getPassword());

        // 2. AuthenticationManager를 통한 인증 수행
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. 인증 성공 시 authentication.getName()으로 사용자명 획득 후 JWT 토큰 생성
        String jwtToken = jwtUtil.generateToken(authentication.getName());

        // 4. 발급된 토큰 반환
        return jwtToken;
    }
}
