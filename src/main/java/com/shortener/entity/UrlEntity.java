package com.shortener.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "url")
public class UrlEntity {
    @Id
    private String id;
    private String originalUrl;

    @Indexed(unique = true)
    private String shortCode;

    private String shortUrl;

    @Indexed(expireAfterSeconds = 3600)
    private Instant createdAt ;

    // Getters & setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Instant getCreatedAT() {
        return createdAt;
    }

    public void setCreatedAT(Instant timeStamp) {
        this.createdAt = timeStamp;
    }
}
