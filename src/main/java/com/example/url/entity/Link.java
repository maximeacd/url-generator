package com.example.url.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link {
    @Id
    private String shortCode;

    @Column(nullable = false, length = 2000)
    private String originalUrl;

    private String title;

    private Instant createdAt;

    private Instant expiresAt;

    private boolean disabled;

    private long accessCount;

    private Instant lastAccessedAt;

    private boolean isCustom;

    private String hash;

    private Long seqId;

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }
}