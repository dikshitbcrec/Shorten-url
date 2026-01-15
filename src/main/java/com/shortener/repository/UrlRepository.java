package com.shortener.repository;

import com.shortener.entity.UrlEntity;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlEntity,String> {


    // Check if a shortCode already exists (collision check)
    boolean existsByShortCode(String shortCode);

    // Find long URL by shortCode (for redirect)
    Optional<UrlEntity> findByShortCode(String shortCode);
}
