package com.hong.bemajor.members;

import com.hong.bemajor.common.ApiResponse;
import com.hong.bemajor.login.LoginRequest;
import com.hong.bemajor.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
        // AuthenticationManager.authenticate() 에서
        // UsernameNotFoundException, BadCredentialsException 등이 발생하면
        // GlobalExceptionHandler로 위임됨
        String token = loginService.authenticate(loginRequest);
        ApiResponse<String> response = ApiResponse.success(token);
        return ResponseEntity.ok(response); // 200 OK
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
