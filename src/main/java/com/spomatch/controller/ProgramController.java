package com.spomatch.controller;

import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramDetailResponseDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
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
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/program")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    // 프로그램 목록 페이지
    @GetMapping("/list")
    public String list(@ModelAttribute ProgramSearchRequestDTO searchDTO, Model model) {
        ProgramListResponseDTO responseDTO = programService.getProgramList(searchDTO);
        model.addAttribute("result", responseDTO);
        return "program/list";
    }

    // 프로그램 목록 API
    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<ProgramListResponseDTO> getProgramListApi(
            @ModelAttribute ProgramSearchRequestDTO searchDTO) {
        return ResponseEntity.ok(programService.getProgramList(searchDTO));
    }

    // 프로그램 상세 페이지
    @GetMapping("/detail/{programId}")
    public String detail(@PathVariable Long programId, Model model) {
        ProgramDetailResponseDTO responseDTO = programService.getProgramDetail(programId);
        model.addAttribute("program", responseDTO);
        return "program/detail";
    }

    // 프로그램 상세 API
    @GetMapping("/api/detail/{programId}")
    @ResponseBody
    public ResponseEntity<ProgramDetailResponseDTO> getProgramDetailApi(
            @PathVariable Long programId) {
        return ResponseEntity.ok(programService.getProgramDetail(programId));
    }

    // 프로그램 조회수 증가
    @PostMapping("/api/view/{programId}")
    @ResponseBody
    public ResponseEntity<Void> increaseViewCount(@PathVariable Long programId) {
        programService.increaseViewCount(programId);
        return ResponseEntity.ok().build();
    }

    // 프로그램 찜하기
    @PostMapping("/api/like/{programId}")
    @ResponseBody
    public ResponseEntity<?> toggleLikeProgram(
            @PathVariable Long programId,
            HttpSession httpSession) {
        // 세션 확인을 위한 로깅 추가
        Long userId = (Long) httpSession.getAttribute("memberId");
        log.info("Current Session ID: {}", httpSession.getId());
        log.info("memberId from session: {}", userId);

        if (userId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 후 이용 가능합니다");
            response.put("redirectUrl", "/login");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        boolean isLiked = programService.toggleLikeProgram(programId, userId);
        return ResponseEntity.ok(isLiked);
    }


}