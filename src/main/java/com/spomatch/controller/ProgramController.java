package com.spomatch.controller;

import com.spomatch.dto.request.ProgramSearchRequestDTO;
import com.spomatch.dto.response.ProgramDetailResponseDTO;
import com.spomatch.dto.response.ProgramListResponseDTO;
import com.spomatch.service.ProgramService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


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
    public ResponseEntity<Boolean> toggleLikeProgram(
            @PathVariable Long programId,
            HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("memberId");
        if (userId == null) {
            // 로그인하지 않은 경우 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isLiked = programService.toggleLikeProgram(programId, userId);
        return ResponseEntity.ok(isLiked);
    }


}