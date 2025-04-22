package com.hong.bemajor.ping;

import com.hong.bemajor.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class PingController {

    @GetMapping("/api/ping")
    public ApiResponse<Map<String, String>> ping() {
        return ApiResponse.success(Collections.singletonMap("ping", "pong"));
    }
}
