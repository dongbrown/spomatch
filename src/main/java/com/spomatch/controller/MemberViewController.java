package com.spomatch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberViewController {

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "/register";
    }
}
