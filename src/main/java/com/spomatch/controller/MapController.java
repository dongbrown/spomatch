package com.spomatch.controller;

import com.spomatch.entity.SportsFacilityProgram;
import com.spomatch.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MapController {

    private final ProgramRepository programRepository;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @GetMapping("/map")
    public String showMap(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        return "/map";
    }

    @GetMapping("/navermap")
    public String showNaverMap(Model model) {
        return "/navermap";
    }

    @GetMapping("/api/facilities")
    @ResponseBody
    public List<SportsFacilityProgram> getFacilities() {
        return programRepository.findAll();
    }
}