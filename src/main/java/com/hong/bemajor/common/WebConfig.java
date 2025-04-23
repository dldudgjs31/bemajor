package com.hong.bemajor.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8080")  // 클라이언트가 위치한 URL을 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);  // 쿠키와 자격 증명을 허용
    }
}