package com.spomatch.controller;

import com.spomatch.dto.ProgramDTO;
import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramDetailResponseDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
import com.spomatch.service.ProgramLikeService;
import com.spomatch.service.ProgramService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/program")
@RequiredArgsConstructor
public class ProgramController {
    private final ProgramService programService;
    private final ProgramLikeService programLikeService;

    @GetMapping("/list")
    public String list(@ModelAttribute ProgramSearchRequestDTO searchDTO, Model model) {
        ProgramListResponseDTO responseDTO = programService.getProgramList(searchDTO);
        model.addAttribute("result", responseDTO);
        return "program/list";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<ProgramListResponseDTO> getProgramListApi(@ModelAttribute ProgramSearchRequestDTO searchDTO) {
        return ResponseEntity.ok(programService.getProgramList(searchDTO));
    }

    @GetMapping("/detail/{programId}")
    public String detail(@PathVariable Long programId, Model model) {
        ProgramDetailResponseDTO responseDTO = programService.getProgramDetail(programId);
        model.addAttribute("program", responseDTO);
        return "program/detail";
    }

    @GetMapping("/api/detail/{programId}")
    @ResponseBody
    public ResponseEntity<ProgramDetailResponseDTO> getProgramDetailApi(@PathVariable Long programId) {
        return ResponseEntity.ok(programService.getProgramDetail(programId));
    }

    @PostMapping("/api/view/{programId}")
    @ResponseBody
    public ResponseEntity<Void> increaseViewCount(@PathVariable Long programId) {
        programService.increaseViewCount(programId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/like/{programId}")
    @ResponseBody
    public ResponseEntity<?> toggleLikeProgram(@PathVariable Long programId, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            Map<String, Object> response = Map.of(
                    "message", "로그인 후 이용 가능합니다",
                    "redirectUrl", "/login"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        boolean isLiked = programLikeService.toggleLike(programId, memberId);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/api/programs/recommended")
    @ResponseBody
    public ResponseEntity<List<ProgramDTO>> getRecommendedPrograms() {
        return ResponseEntity.ok(programLikeService.getTopLikedPrograms());
    }

    @GetMapping("/api/programs/liked")
    @ResponseBody
    public ResponseEntity<?> getLikedPrograms(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            Map<String, Object> response = Map.of(
                    "message", "로그인 후 이용 가능합니다",
                    "redirectUrl", "/login"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(programLikeService.getLikedPrograms(memberId));
    }
}