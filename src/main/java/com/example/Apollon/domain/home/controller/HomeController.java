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

        return "3조 화이팅!!!!!!!!슈루루루룽1번째 슈루루루룽2번째ㅋㅋㅋ";




    }
}
