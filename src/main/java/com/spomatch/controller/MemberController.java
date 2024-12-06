package com.spomatch.controller;

import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.CreateMemberRequest;
import com.spomatch.dto.response.MemberResponse;
import com.spomatch.dto.response.ProgramListResponseDTO;
import com.spomatch.entity.Member;
import com.spomatch.service.MemberService;
import com.spomatch.service.ProgramService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody CreateMemberRequest request) {
        Member savedMember = memberService.save(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MemberResponse(savedMember));
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    // 찜 목록 확인 API
    @GetMapping("/liked-programs")
    public String getLikedPrograms(HttpSession httpSession, Model model) {
        Long memberId = (Long) httpSession.getAttribute("memberId");

        if (memberId == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        List<ProgramDTO> likedPrograms = programService.selectLikedProgramList(memberId);
        model.addAttribute("likedPrograms", likedPrograms);
        return "member/likedPrograms"; // JSP 경로 반환
    }
}
