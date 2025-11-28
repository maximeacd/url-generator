package com.example.url.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class UpdateLinkRequest {
    private String title;
    private Instant expiresAt;
    private Boolean disabled;
}