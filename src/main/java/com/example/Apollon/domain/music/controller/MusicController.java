package com.example.Apollon.domain.music.controller;

import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.post.entity.Post;
import com.example.Apollon.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/chart")
@RequiredArgsConstructor
public class MusicController {
    private final MusicService musicService;

    // TOP100
    @GetMapping("/TOP100")
    public String top100List(Model model) {
        List<Music> top100List = this.musicService.getTOP100List();

        model.addAttribute("TOP100", top100List);

        return "chart/TOP100";
    }


    // 곡 정보
    @GetMapping("/musicDetail/{id}")
    public String musicDetail(Model model, @PathVariable("id") Long id) {

        Music m = musicService.getMusic(id);

        model.addAttribute("music", m);

        return "music/musicDetail";
    }
}