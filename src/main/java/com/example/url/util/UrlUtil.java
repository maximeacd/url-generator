package com.example.url.util;

import java.net.URI;
import java.security.MessageDigest;
import java.util.Locale;

public class UrlUtil {
    public static String normalize(String input) throws Exception {
        if (input == null) throw new IllegalArgumentException("url required");
        String trimmed = input.trim();
        URI uri = new URI(trimmed);
        String scheme = uri.getScheme();
        if (scheme == null) throw new IllegalArgumentException("missing scheme");
        scheme = scheme.toLowerCase(Locale.ROOT);
        int port = uri.getPort();
        if (port == -1 || (scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443)) {
            port = -1;
        }
        String host = uri.getHost();
        if (host == null) throw new IllegalArgumentException("missing host");
        host = host.toLowerCase(Locale.ROOT);
        String path = uri.getRawPath();
        String query = uri.getRawQuery();
        String fragment = uri.getRawFragment();

        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://").append(host);
        if (!(port == -1 || (scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443))) {
            sb.append(":").append(port);
        }
        if (path != null) sb.append(path);
        if (query != null) sb.append("?").append(query);
        if (fragment != null) sb.append("#").append(fragment);
        return sb.toString();
    }

    public static String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}