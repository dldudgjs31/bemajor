package com.hong.bemajor.members;

import com.hong.bemajor.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
        ApiResponse<String> response = ApiResponse.success("Member created successfully");
        return ResponseEntity.status(201).body(response);  // 201 Created
    }
}
