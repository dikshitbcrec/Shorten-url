package com.shortener.service;

import com.shortener.dto.UrlRequest;

public interface TinyUrlService {
    String generateShortUrl(UrlRequest urlEntity);
    String getUrl(String shortCode);
}
