package com.spomatch.controller;

import com.spomatch.dto.request.CreateMemberRequest;
import com.spomatch.dto.request.LoginMemberRequest;
import com.spomatch.dto.response.LoginResponse;
import com.spomatch.dto.response.MemberResponse;
import com.spomatch.entity.Member;
import com.spomatch.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody CreateMemberRequest request) {
        Member savedMember = memberService.save(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MemberResponse(savedMember));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginMemberRequest request) {
        try {
            Member member = memberService.login(request);
            return ResponseEntity.ok()
                    .body(new LoginResponse("로그인 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(e.getMessage()));
        }
    }
}
