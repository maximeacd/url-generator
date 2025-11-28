package com.example.url.repository;

import com.example.url.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, String> {
    Optional<Link> findByHash(String hash);
    boolean existsByShortCode(String shortCode);
}