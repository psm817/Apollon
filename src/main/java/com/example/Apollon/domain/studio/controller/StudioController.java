package com.example.Apollon.domain.studio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/studio")
@RequiredArgsConstructor
public class StudioController {
    @GetMapping("/")
    public String mine() {

        return "studio_detail";
    }
}
