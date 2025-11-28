package com.example.url.dto;

import com.example.url.entity.Link;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class LinkResponse {

    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Instant createdAt;
    private Instant expiresAt;

    public static LinkResponse from(Link link, String baseUrl) {
        LinkResponse res = new LinkResponse();
        res.shortCode = link.getShortCode();
        res.shortUrl = baseUrl + "/api/redirect/" + link.getShortCode();
        res.originalUrl = link.getOriginalUrl();
        res.createdAt = link.getCreatedAt();
        res.expiresAt = link.getExpiresAt();
        return res;
    }
}