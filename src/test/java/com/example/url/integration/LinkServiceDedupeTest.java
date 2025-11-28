package com.example.url.integration;

import com.example.url.dto.ShortenRequest;
import com.example.url.entity.Link;
import com.example.url.exception.ConflictException;
import com.example.url.exception.GoneException;
import com.example.url.repository.LinkRepository;
import com.example.url.service.LinkService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class LinkServiceDedupeTest {

    @Autowired
    private LinkService service;

    @Autowired
    private LinkRepository repo;

    @Autowired
    private EntityManager em;

    @BeforeEach
    @Transactional
    public void init() {
        em.createNativeQuery("CREATE SEQUENCE IF NOT EXISTS seq_shorturl START WITH 1 INCREMENT BY 1").executeUpdate();
    }

    @Test
    void returnsExistingLinkForSameUrl() throws Exception {
        ShortenRequest req = new ShortenRequest();
        req.setUrl("https://example.com/test");

        Link first = service.createOrGetExisting(req);
        Link second = service.createOrGetExisting(req);

        assertEquals(first.getShortCode(), second.getShortCode());
    }

    @Test
    void throwsConflictForTakenCustomAlias() throws Exception {
        ShortenRequest req1 = new ShortenRequest();
        req1.setUrl("https://example.com/1");
        req1.setCustomAlias("myalias");

        ShortenRequest req2 = new ShortenRequest();
        req2.setUrl("https://example.com/2");
        req2.setCustomAlias("myalias");

        service.createOrGetExisting(req1);
        assertThrows(ConflictException.class, () -> service.createOrGetExisting(req2));
    }

    @Test
    void expiredLinkThrowsGone() throws Exception {
        ShortenRequest req = new ShortenRequest();
        req.setUrl("https://example.com");
        req.setExpiresAt(Instant.now().plusSeconds(60));
        Link link = service.createOrGetExisting(req);
        link.setExpiresAt(Instant.now().minusSeconds(10));
        repo.save(link);
        assertThrows(GoneException.class, () -> service.access(link.getShortCode()));
    }
}