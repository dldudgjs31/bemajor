package com.hong.bemajor.common;

import com.hong.bemajor.login.JwtAuthenticationFilter;
import com.hong.bemajor.login.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * 스프링 시큐리티 설정 클래스
 * - 웹 보안, JWT 인증 필터, DAO 인증 프로바이더 구성
 * - 메소드단 권한 체크(@Secured, @PreAuthorize) 활성화
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,    // @PreAuthorize, @PostAuthorize 애노테이션 사용
    securedEnabled   = true   // @Secured 애노테이션 사용
)
public class SecurityConfig {
    private final MyUserDetailsService myUserDetailsService;
    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(MyUserDetailsService myUserDetailsService, JwtAuthenticationFilter jwtFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * 비밀번호 암호화 방식 설정
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * SecurityFilterChain 설정
     * - CSRF 비활성화 (JWT 기반 인증이므로 세션 사용 안 함)
     * - Stateless 세션 정책 설정
     * - 로그인 엔드포인트는 허용, 그 외 요청은 인증 필요
     * - JWT 필터를 UsernamePasswordAuthenticationFilter 이전에 등록
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          // 1) CSRF: 쿠키 기반 토큰 저장, 인증 필요 없는 경로 제외
          .csrf(csrf -> csrf
              .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
              .ignoringAntMatchers(
                  "/api/members/login",
                  "/api/ping",
                  "/api/orders/**",
                  "/assets/**",
                  "/vendors/**",
                  "/js/**"
              )
          )
          // 2) 세션 없이 JWT만으로 인증
          .sessionManagement(sm -> sm
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )
          // 3) URL별 인가
          .authorizeRequests(auth -> auth
              // 뷰 페이지
              .antMatchers("/", "/index", "/orders/**").permitAll()
              // 인증, 헬스체크 등
              .antMatchers(
                  "/api/members/login",
                  "/api/ping",
                  "/api/orders/**"
              ).permitAll()
              // 정적 리소스
              .antMatchers(
                  "/assets/**",
                  "/vendors/**",
                  "/js/**"
              ).permitAll()
              // 그 외 요청은 인증 필요
              .anyRequest().authenticated()
          )
          // 4) JWT 필터 적용
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * DAO 기반 AuthenticationProvider 구성
     * - UserDetailsService, PasswordEncoder 설정
     * @return DaoAuthenticationProvider 인스턴스
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager 빈 등록
     * - DAO 인증 프로바이더를 AuthenticationManager에 설정
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.authenticationProvider(daoAuthenticationProvider());
        return authBuilder.build();
    }
}
