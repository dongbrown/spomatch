package com.spomatch.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MemberViewController {

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request, HttpSession session) {
        String referer = request.getHeader("Referer");
        log.info("=== 로그인 페이지 접근 ===");
        log.info("현재 Referer: {}", referer);
        log.info("현재 세션 ID: {}", session.getId());
        log.info("현재 저장된 originPage: {}", session.getAttribute("originPage"));

        // 현재 저장된 originPage가 없고, 이전 페이지가 로그인/회원가입이 아닐 경우에만 저장
        if (session.getAttribute("originPage") == null
                && referer != null
                && !referer.contains("/login")
                && !referer.contains("/register")) {
            session.setAttribute("originPage", referer);
            log.info("최초 접근 페이지 저장 완료: {}", referer);
        }

        return "/login";
    }

    @GetMapping("/register")
    public String registerForm(HttpServletRequest request, HttpSession session) {
        String referer = request.getHeader("Referer");

        if (referer != null && !referer.contains("/register")) {
            // 이전 페이지가 로그인 페이지라면, originPage는 유지
            if (!referer.contains("/login")) {
                session.setAttribute("originPage", referer);
            }
            session.setAttribute("prevPage", referer);
            log.info("회원가입 - 최초 페이지: {}, 이전 페이지: {}",
                    session.getAttribute("originPage"), referer);
        }

        return "/register";
    }
}