package com.example.url.controller;

import com.example.url.dto.UpdateLinkRequest;
import com.example.url.entity.Link;
import com.example.url.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
public class LinkManagementController {

    private final LinkService linkService;

    public LinkManagementController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> get(@PathVariable String code) {
        Link link = linkService.get(code);
        return ResponseEntity.ok(link);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(@PathVariable String code, @RequestBody UpdateLinkRequest req) {
        Link updated = linkService.update(code, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        linkService.delete(code);
        return ResponseEntity.noContent().build();
    }
}