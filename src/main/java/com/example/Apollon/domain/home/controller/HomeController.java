package com.example.Apollon.domain.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    public String test() {

        return "/layout";

    }
}
