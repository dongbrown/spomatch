package com.spomatch.service;

import com.spomatch.dto.request.LoginMemberRequest;
import com.spomatch.entity.Member;
import com.spomatch.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;

    public Member save(Member member) {
        member.encodePassword(passwordEncoder);
        return memberRepository.save(member);
    }

    public Member login(LoginMemberRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("아이디를 확인해주세요."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 확인해주세요.");
        }

        httpSession.setAttribute("memberId", member.getId());
        log.info("Session saved - memberId: {}, sessionId: {}",
                member.getId(), httpSession.getId());

        return member;
    }
}
