package com.spomatch.config;

import com.spomatch.entity.Member;
import com.spomatch.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("=== 로그인 성공 핸들러 시작 ===");

        HttpSession session = request.getSession();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // returnUrl 파라미터 확인 (POST 파라미터와 쿼리 파라미터 모두 확인)
        String returnUrl = request.getParameter("returnUrl");
        if (returnUrl == null) {
            returnUrl = request.getQueryString();
            if (returnUrl != null && returnUrl.contains("returnUrl=")) {
                returnUrl = returnUrl.split("returnUrl=")[1];
            }
        }
        log.info("returnUrl: {}", returnUrl);

        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        session.setAttribute("memberId", member.getId());

        // returnUrl이 있으면 해당 URL로, 없으면 메인으로
        if (returnUrl != null && !returnUrl.isEmpty()) {
            // URL 디코딩
            String decodedUrl = URLDecoder.decode(returnUrl, StandardCharsets.UTF_8);
            log.info("디코딩된 returnUrl로 리다이렉트: {}", decodedUrl);
            response.sendRedirect(decodedUrl);
        } else {
            log.info("메인으로 리다이렉트");
            response.sendRedirect("/");
        }

        log.info("=== 로그인 성공 핸들러 종료 ===");
    }
}