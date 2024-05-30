package com.example.Apollon.domain.music.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MusicController {
    @GetMapping("/music")
    public String musicPlayer(Model model) {
        String[] playlist = {"곡1", "곡2", "곡3", "곡4", "곡5"};
        model.addAttribute("playlist", playlist);
        return "redirect:/";
    }
}
