package com.hong.bemajor.ping;

import com.hong.bemajor.common.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Controller
public class PingController {
    @GetMapping("/test")
    public String home(Model model) {
        return "test";
    }

    @GetMapping("/api/ping")
    @ResponseBody
    public ApiResponse<Map<String, String>> ping() {
        return ApiResponse.success(Collections.singletonMap("ping", "pong"));
    }
}
