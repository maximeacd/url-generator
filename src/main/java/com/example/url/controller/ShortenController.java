package com.example.url.controller;

import com.example.url.dto.ShortenRequest;
import com.example.url.dto.LinkResponse;
import com.example.url.entity.Link;
import com.example.url.service.LinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/shorten")
public class ShortenController {

    private final LinkService service;

    @Value("${app.base-url}")
    private String baseUrl;

    public ShortenController(LinkService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> shorten(@RequestBody ShortenRequest req){
        try {
            Link link = service.createOrGetExisting(req);
            boolean exists = link.getAccessCount() > 0;
            return ResponseEntity.status(exists ? 200 : 201)
                    .body(LinkResponse.from(link, baseUrl));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}