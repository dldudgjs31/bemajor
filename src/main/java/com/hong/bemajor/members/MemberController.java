package com.hong.bemajor.members;

import com.hong.bemajor.common.ApiResponse;
import com.hong.bemajor.login.LoginRequest;
import com.hong.bemajor.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;

    @Autowired
    public MemberController(MemberService memberService, LoginService loginService) {
        this.memberService = memberService;
        this.loginService = loginService;
    }
    // 로그인 (POST /api/members/login)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = loginService.authenticate(loginRequest);  // 로그인 인증 후 JWT 토큰 발급
            ApiResponse<String> response = ApiResponse.success(token);  // 성공적인 응답
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            // 사용자 없을 때
            return ResponseEntity.status(404)
                .body(ApiResponse.error(e.getMessage()));
        } catch (BadCredentialsException e) {
            // 비밀번호 불일치
            return ResponseEntity.status(401)
                .body(ApiResponse.error("비밀번호가 일치하지 않습니다."));
        } catch (Exception e) {
            // 그 외
            return ResponseEntity.status(500)
                .body(ApiResponse.error("인증 처리 중 오류가 발생했습니다."));
        }
    }

    // 회원 목록 조회 (GET /api/members)
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberDto>>> getMembers() {
        List<MemberDto> members = memberService.getAllMembers();
        ApiResponse<List<MemberDto>> response = ApiResponse.success(members);
        return ResponseEntity.ok(response);  // 200 OK
    }

    // 회원 등록 (POST /api/members)
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createMember(@Valid @RequestBody MemberDto newMember) {
        memberService.addMember(newMember);
        ApiResponse<String> response = ApiResponse.success("계정 생성 완료");
        return ResponseEntity.status(201).body(response);  // 201 Created
    }
}
