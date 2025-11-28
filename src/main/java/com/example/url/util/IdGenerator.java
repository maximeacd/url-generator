package com.example.url.util;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IdGenerator {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public long nextId() {
        return (Long) em.createNativeQuery("SELECT NEXTVAL('seq_shorturl')").getSingleResult();
    }

    @Transactional
    public String generateUniqueCode() {
        long id = nextId();
        return toBase62(id);
    }

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = ALPHABET.length();

    private String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}