package com.example.Apollon.domain.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    @ResponseBody
    public String test() {
        String title;
        String body;
        String body1;
        String body2;
        String body3;
        String body4;
        String body5;
        String body6;

        return "3조 화이팅!!!!!!!!!!!!!!!!!!!!!!!!!!";
    }
}
