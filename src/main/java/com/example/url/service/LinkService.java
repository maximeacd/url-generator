package com.example.url.service;

import com.example.url.dto.ShortenRequest;
import com.example.url.dto.UpdateLinkRequest;
import com.example.url.entity.Link;
import com.example.url.exception.ConflictException;
import com.example.url.exception.GoneException;
import com.example.url.exception.NotFoundException;
import com.example.url.repository.LinkRepository;
import com.example.url.util.IdGenerator;
import com.example.url.util.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Optional;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repo;

    @Autowired
    private IdGenerator idGenerator;

    public Link createOrGetExisting(ShortenRequest req) throws Exception {
        if (req.getUrl() == null || req.getUrl().isBlank()) {
            throw new IllegalArgumentException("URL is required");
        }
        String normalized = UrlUtil.normalize(req.getUrl());
        try {
            URI uri = new URI(normalized);
            if (!("http".equals(uri.getScheme()) || "https".equals(uri.getScheme()))) {
                throw new IllegalArgumentException("URL must start with http:// or https://");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL format");
        }

        String hash = UrlUtil.sha256Hex(normalized);
        Optional<Link> existing = repo.findByHash(hash);
        if (existing.isPresent()) return existing.get();

        String code = req.getCustomAlias();
        if (code != null) {
            if (!code.matches("^[A-Za-z0-9_-]{3,64}$")) {
                throw new IllegalArgumentException("Custom alias must be 3-64 chars, A-Z a-z 0-9 _ or -");
            }
            if (repo.existsById(code)) {
                throw new ConflictException("Alias already in use");
            }
        } else {
            code = idGenerator.generateUniqueCode();
        }

        Instant expiresAt = null;
        if (req.getExpiresAt() != null) {
            Instant instant = req.getExpiresAt();
            if (instant.isBefore(Instant.now())) {
                throw new IllegalArgumentException("TTL must be in the future");
            }
            expiresAt = instant;
        }

        Link link = new Link();
        link.setShortCode(code);
        link.setOriginalUrl(normalized);
        link.setHash(hash);
        link.setCreatedAt(Instant.now());
        link.setExpiresAt(expiresAt);
        link.setTitle(req.getTitle());
        link.setAccessCount(0);
        link.setCustom(req.getCustomAlias() != null);
        link.setDisabled(false);

        return repo.save(link);
    }

    public Link get(String code) {
        return repo.findById(code).orElseThrow(() -> new NotFoundException("Short code not found"));
    }

    public Link access(String code) {
        Link link = get(code);
        if (link.isExpired()) {
            throw new GoneException("Link expired");
        }
        if (link.isDisabled()) {
            throw new NotFoundException("Link disabled");
        }
        link.setAccessCount(link.getAccessCount() + 1);
        link.setLastAccessedAt(Instant.now());
        return repo.save(link);
    }

    public Link update(String code, UpdateLinkRequest req) {
        Link link = get(code);
        if (req.getTitle() != null) link.setTitle(req.getTitle());
        if (req.getExpiresAt() != null) link.setExpiresAt(req.getExpiresAt());
        if (req.getDisabled() != null) link.setDisabled(req.getDisabled());
        return repo.save(link);
    }

    public void delete(String code) {
        Link link = get(code);
        link.setDisabled(true);
        repo.save(link);
    }
}