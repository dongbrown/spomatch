package com.spomatch.service;

import com.spomatch.entity.Member;
import com.spomatch.repository.MemberRepository;
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

    public Member save(Member member) {
        // 저장 전 로그
        log.info("=== 회원가입 시작 ===");
        log.info("가입 시도 ID: {}", member.getLoginId());
        log.info("인코딩 전 비밀번호: {}", member.getPassword());

        member.encodePassword(passwordEncoder);
        log.info("인코딩 후 비밀번호: {}", member.getPassword());

        Member savedMember = memberRepository.save(member);
        log.info("=== 회원가입 완료 ===");
        log.info("저장된 회원 ID: {}", savedMember.getLoginId());
        log.info("저장된 비밀번호: {}", savedMember.getPassword());

        return savedMember;
    }
}
