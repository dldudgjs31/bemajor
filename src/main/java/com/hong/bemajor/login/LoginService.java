package com.hong.bemajor.login;

import com.hong.bemajor.members.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberDao memberDao;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MemberDao memberDao) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.memberDao = memberDao;
    }

    // 로그인 처리: 아이디와 비밀번호를 이용해 사용자 인증 후 JWT 토큰 발급
    public String authenticate(LoginRequest loginRequest) throws AuthenticationException {

        // 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin_id(), loginRequest.getPassword())
        );

        // 인증 성공 시 JWT 토큰 생성
        return jwtUtil.generateToken(authentication.getName());

    }
}
