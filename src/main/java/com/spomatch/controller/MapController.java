package com.spomatch.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MapController {
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
}