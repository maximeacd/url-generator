package com.example.url.controller;

import com.example.url.entity.Link;
import com.example.url.service.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

@RestController
@RequestMapping("/api/redirect")
public class RedirectController {

    private final LinkService linkService;

    public RedirectController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> redirect(@PathVariable String code,
            @RequestHeader(value = "Accept", required = false) String accept,
            HttpServletRequest request) {

        Link link = linkService.access(code);

        if (link == null) {
            return ResponseEntity.notFound().build();
        }

        if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(Instant.now())) {
            return ResponseEntity.status(410).build();
        }

        String url = link.getOriginalUrl();
        String query = request.getQueryString();

        if (query != null && !query.isBlank()) {
            url += "?" + query;
        }

        return ResponseEntity.status(301)
                .header("Location", url)
                .build();
    }
}