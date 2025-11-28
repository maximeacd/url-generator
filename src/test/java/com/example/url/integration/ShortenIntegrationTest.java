package com.example.url.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void createAndRedirect() {
        Map<String, Object> req = Map.of("url", "https://example.com/test");
        ResponseEntity<Map> response = rest.postForEntity("/api/shorten", req, Map.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        String code = (String) body.get("shortCode");
        assertNotNull(code);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "text/plain");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> redirectResp = rest.exchange("/" + code, HttpMethod.GET, entity, String.class);

        assertEquals(301, redirectResp.getStatusCode().value());
        assertTrue(Objects.requireNonNull(redirectResp.getHeaders().getLocation())
                .toString().contains("example.com/test"));
    }

    @Test
    void expiredLinkReturns301() {
        Map<String, Object> req = Map.of(
                "url", "https://example.com/expired",
                "ttl", Instant.now().minusSeconds(1)
        );
        ResponseEntity<Map> resp = rest.postForEntity("/api/shorten", req, Map.class);
        assertTrue(resp.getStatusCode().is2xxSuccessful());
        String code = (String) resp.getBody().get("shortCode");

        ResponseEntity<Map> result = rest.getForEntity("/" + code, Map.class);
        assertEquals(301, result.getStatusCode().value());
    }

    @Test
    void missingLinkReturns404() {
        ResponseEntity<Map> resp = rest.getForEntity("/notfound123", Map.class);
        assertEquals(404, resp.getStatusCode().value());
    }

    @Test
    void updateLink() {
        Map<String, Object> req = Map.of("url", "https://example.com/update");
        ResponseEntity<Map> resp = rest.postForEntity("/api/shorten", req, Map.class);
        String code = (String) resp.getBody().get("shortCode");

        Map<String, Object> update = Map.of("title", "Updated Title");
        rest.put("/api/links/" + code, update);

        ResponseEntity<Map> linkResp = rest.getForEntity("/api/links/" + code, Map.class);
        assertEquals("Updated Title", linkResp.getBody().get("title"));
    }

    @Test
    void deleteLinkDisablesIt() {
        Map<String, Object> req = Map.of("url", "https://example.com/delete");
        ResponseEntity<Map> resp = rest.postForEntity("/api/shorten", req, Map.class);
        String code = (String) resp.getBody().get("shortCode");

        rest.delete("/api/links/" + code);

        ResponseEntity<Map> result = rest.getForEntity("/" + code, Map.class);
        assertEquals(404, result.getStatusCode().value());
    }
}