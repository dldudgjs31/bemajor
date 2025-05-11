package com.hong.bemajor.users;

import com.hong.bemajor.common.ApiResponse;
import com.hong.bemajor.login.LoginRequest;
import com.hong.bemajor.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {

    private final UsersService usersService;
    private final LoginService loginService;

    @Autowired
    public UsersRestController(UsersService usersService, LoginService loginService) {
        this.usersService = usersService;
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

    // 회원 목록 조회 (GET /api/users)
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsersDto>>> getUsers() {
        List<UsersDto> users = usersService.getAllUsers();
        ApiResponse<List<UsersDto>> response = ApiResponse.success(users);
        return ResponseEntity.ok(response);  // 200 OK
    }

    // 회원 등록 (POST /api/users)
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createUsers(@Valid @RequestBody UsersDto usersDto) {
        usersService.addUser(usersDto);
        ApiResponse<String> response = ApiResponse.success("계정 생성 완료");
        return ResponseEntity.status(201).body(response);  // 201 Created
    }
}
