package com.example.url.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class ShortenRequest {
    private String url;
    private String customAlias;
    private Instant expiresAt;
    private String title;
}