package com.example.Apollon.domain.playlist.controller;

import com.example.Apollon.domain.comment.service.CommentService;
import com.example.Apollon.domain.member.entity.Member;
import com.example.Apollon.domain.member.service.MemberService;
import com.example.Apollon.domain.music.entity.Music;
import com.example.Apollon.domain.music.service.MusicService;
import com.example.Apollon.domain.playlist.entity.Playlist;
import com.example.Apollon.domain.playlist.service.PlaylistService;
import com.example.Apollon.domain.studio.service.StudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@RequestMapping("/playlist")
public class PlaylistController {
    private final MemberService memberService;
    private final MusicService musicService;
    private final PlaylistService playlistService;

    @PostMapping("/addSong")
    public String addSongToPlaylist(@RequestParam Long memberId, @RequestParam Long musicId) {
        Playlist playlist = playlistService.getPlaylist(memberId);

        Music newMusic = musicService.getMusicById(musicId);
        playlist.addMusic(newMusic);

        playlistService.savePlaylist(playlist);

        return "redirect:/playlist/view?memberId=" + memberId;
    }

    @GetMapping("/play")
    public String playPlaylist(@RequestParam Long playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        musicService.playPlaylist(playlist);

        return "redirect:/playlist/view?playlistId=" + playlistId;
    }

    @GetMapping("/view")
    public String viewPlaylist(@RequestParam Long playlistId, Model model) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        model.addAttribute("playlist", playlist);

        return "playlist/view";
    }

    @GetMapping("/music/play/{id}")
    public ResponseEntity<Resource> playMusic(@PathVariable("id") Long id, @RequestHeader HttpHeaders headers) {
        Music music = musicService.getMusic(id);
        String musicFilePath = music.getMusicMp3();

        String fileDirPath = "src/main/resources/static/uploadFile";
        Path path = Paths.get(fileDirPath, musicFilePath);

        // 파일이 존재하지 않을 경우 404 응답 반환
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(path.toFile());

        if (!resource.exists()) {
            throw new RuntimeException("Music file not found");
        }

        long fileLength;
        try {
            fileLength = Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not determine file size", e);
        }

        if (headers.getRange().isEmpty()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength))
                    .body(resource);
        } else {
            HttpRange range = headers.getRange().get(0);
            long start = range.getRangeStart(fileLength);
            long end = range.getRangeEnd(fileLength);

            if (end >= fileLength) {
                end = fileLength - 1;
            }

            long contentLength = end - start + 1;

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength)
                    .body(new FileSystemResource(path.toFile()) {
                        @Override
                        public InputStream getInputStream() throws IOException {
                            InputStream inputStream = super.getInputStream();
                            inputStream.skip(start);
                            return new LimitedInputStream(inputStream, contentLength);
                        }
                    });
        }
    }

    private boolean isValidPath(Path path) {
        return Files.exists(path) && !Files.isDirectory(path);
    }

    private static class LimitedInputStream extends InputStream {
        private final InputStream delegate;
        private final long maxLength;
        private long currentPosition = 0;

        public LimitedInputStream(InputStream delegate, long maxLength) {
            this.delegate = delegate;
            this.maxLength = maxLength;
        }

        @Override
        public int read() throws IOException {
            if (currentPosition >= maxLength) {
                return -1;
            }
            currentPosition++;
            return delegate.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (currentPosition >= maxLength) {
                return -1;
            }
            len = (int) Math.min(len, maxLength - currentPosition);
            int bytesRead = delegate.read(b, off, len);
            if (bytesRead == -1) {
                return -1;
            }
            currentPosition += bytesRead;
            return bytesRead;
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }

}
