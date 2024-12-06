package com.spomatch.config;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthSuccessHandler authSuccessHandler;
    private final CustomAuthFailureHandler authFailureHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                            .sessionFixation().none()
                            .maximumSessions(1)
                            .maxSessionsPreventsLogin(false);
                    log.info("세션 관리 설정 완료");
                })
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/api/auth/status").permitAll()
                            .anyRequest().permitAll();
                    log.info("URL 권한 설정 완료");
                })
                .formLogin(form -> {
                    form.loginPage("/login")
                            .loginProcessingUrl("/login")
                            .usernameParameter("loginId")
                            .passwordParameter("password")
                            .successHandler(authSuccessHandler)
                            .failureHandler(authFailureHandler);
                    log.info("로그인 폼 설정 완료");
                })
                .requestCache(cache -> {
                    cache.requestCache(new HttpSessionRequestCache());  // 요청 캐시 설정 추가
                    log.info("요청 캐시 설정 완료");
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            // 로그아웃 전에 세션 ID 로깅
                            HttpSession session = request.getSession(false);
                            if (session != null) {
                                String sessionId = session.getId();
                                String username = authentication != null ? authentication.getName() : "Unknown";
                                log.info("로그아웃 시작! 세션 ID: {}", sessionId);
                                log.info("로그아웃 사용자: {}", username);
                            }
                        })
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.sendRedirect("/?logout=success");  // 로그아웃 파라미터 추가
                        })
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}