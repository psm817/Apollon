    package com.example.Apollon.domain.music.controller;

    import com.example.Apollon.domain.member.entity.Member;
    import com.example.Apollon.domain.member.service.MemberService;
    import com.example.Apollon.domain.music.entity.Music;
    import com.example.Apollon.domain.music.service.MusicService;
    import jakarta.servlet.http.HttpServletRequest;
    import lombok.RequiredArgsConstructor;
    import org.springframework.core.io.FileSystemResource;
    import org.springframework.core.io.Resource;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpRange;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.io.InputStream;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.security.Principal;
    import java.util.List;

    @Controller
    @RequestMapping("/chart")
    @RequiredArgsConstructor
    public class MusicController {
        private final MusicService musicService;
        private final MemberService memberService;

        // TOP100
        @GetMapping("/TOP100")
        public String top100List(Model model) {
            List<Music> top100List = this.musicService.getTOP100List();

            model.addAttribute("TOP100", top100List);

            return "chart/TOP100";
        }


        // 곡 정보 상세보기
        @GetMapping("/musicDetail/{id}")
        public String musicDetail(Model model, @PathVariable("id") Long id) {

            Music m = musicService.getMusic(id);

            model.addAttribute("music", m);

            return "music/musicDetail";
        }

        @PreAuthorize("isAuthenticated()")
        @GetMapping("/music/{id}/like")
        public String likeOrUnlikeMusic(Principal principal, @PathVariable("id") Long id, HttpServletRequest request) {
            Music music = this.musicService.getMusic(id);
            Member member = this.memberService.getMember(principal.getName());

            if (music.getLikedByMembers().contains(member)) {
                this.musicService.unlikeMusic(music.id, member.id);
            } else {
                this.musicService.likeMusic(music.id, member.id);
            }

            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }

        // 음악 파일 재생
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