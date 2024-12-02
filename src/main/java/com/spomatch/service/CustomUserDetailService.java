package com.spomatch.service;

import com.spomatch.entity.Member;
import com.spomatch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("===== loadUserByUsername 시작 =====");
        log.info("전달받은 loginId: {}", loginId);

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        log.info("DB에서 찾은 회원 정보:");
        log.info("ID: {}", member.getLoginId());
        log.info("저장된 비밀번호: {}", member.getPassword());

        return User.builder()
                .username(member.getLoginId())
                .password(member.getPassword())
                .roles("USER")
                .build();
    }
}
