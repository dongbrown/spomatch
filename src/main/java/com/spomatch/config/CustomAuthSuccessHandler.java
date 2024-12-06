package com.spomatch.config;

import com.spomatch.entity.Member;
import com.spomatch.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        HttpSession session = request.getSession();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Member member = memberRepository.findByLoginId(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // memberId와 함께 name도 세션에 저장
        session.setAttribute("memberId", member.getId());
        session.setAttribute("memberName", member.getName());

        log.info("로그인 성공! 세션 ID: {}, memberId: {}, memberName: {}",
                session.getId(),
                session.getAttribute("memberId"),
                session.getAttribute("memberName"));

        response.sendRedirect("/");
    }
}