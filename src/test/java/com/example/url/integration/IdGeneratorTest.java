package com.example.url.integration;

import com.example.url.util.IdGenerator;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class IdGeneratorTest {

    @Autowired
    private IdGenerator generator;

    @Autowired
    private EntityManager em;

    @BeforeEach
    @Transactional
    public void init() {
        em.createNativeQuery("CREATE SEQUENCE IF NOT EXISTS seq_shorturl START WITH 1 INCREMENT BY 1").executeUpdate();
    }

    @Test
    void generatesUniqueCodes() {
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String code = generator.generateUniqueCode();
            assertFalse(code.isEmpty());
            assertTrue(codes.add(code), "Code should be unique");
        }
    }
}