package com.spomatch.controller;

import com.spomatch.dto.request.CreateMemberRequest;
import com.spomatch.dto.response.MemberResponse;
import com.spomatch.entity.Member;
import com.spomatch.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody CreateMemberRequest request,
                                          HttpSession session) {
        try {
            Member savedMember = memberService.save(request.toEntity());

            // 이전 페이지 확인
            String originPage = (String) session.getAttribute("originPage"); // 최초 시작 페이지
            String prevPage = (String) session.getAttribute("prevPage");     // 직전 페이지

            String redirectUrl = "/login";  // 기본적으로 로그인 페이지로

            // 직전 페이지가 로그인 페이지이고, 최초 시작 페이지가 있다면 로그인 페이지로 가되 returnUrl 파라미터 추가
            if (prevPage != null && prevPage.contains("/login") && originPage != null) {
                redirectUrl = "/login?returnUrl=" + URLEncoder.encode(originPage, "UTF-8");
            }

            log.info("회원가입 성공 - 최초 페이지: {}, 이전 페이지: {}, 리다이렉트: {}",
                    originPage, prevPage, redirectUrl);

            // 세션 정리
            session.removeAttribute("originPage");
            session.removeAttribute("prevPage");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "member", new MemberResponse(savedMember),
                            "redirectUrl", redirectUrl,
                            "message", "회원가입이 완료되었습니다. 로그인해주세요."
                    ));
        } catch (Exception e) {
            log.error("회원가입 실패", e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}