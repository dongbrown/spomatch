package com.spomatch.controller;

import com.spomatch.dto.CreateMemberRequest;
import com.spomatch.dto.MemberResponse;
import com.spomatch.entity.Member;
import com.spomatch.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody CreateMemberRequest request) {
        Member savedMember = memberService.save(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MemberResponse(savedMember));
    }
}
