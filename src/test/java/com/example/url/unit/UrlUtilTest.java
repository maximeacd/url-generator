package com.example.url.unit;

import com.example.url.util.UrlUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlUtilTest {

    @Test
    void normalizesUrlCorrectly() throws Exception {
        String raw = "  HTTP://Example.com:80/path?query=1#frag ";
        String normalized = UrlUtil.normalize(raw);
        assertEquals("http://example.com/path?query=1#frag", normalized);
    }

    @Test
    void throwsOnMissingScheme() {
        assertThrows(IllegalArgumentException.class, () -> UrlUtil.normalize("example.com/path"));
    }
}