package com.shortener.service;

import com.shortener.dto.UrlRequest;
import com.shortener.dto.UrlResponse;

public interface TinyUrlService {
    UrlResponse generateShortUrl(UrlRequest urlEntity);
    String getUrl(String shortCode);
    Long getTotalCount();
}
