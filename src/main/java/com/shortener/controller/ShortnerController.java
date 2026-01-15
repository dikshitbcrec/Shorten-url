package com.shortener.controller;

import com.shortener.dto.UrlRequest;
import com.shortener.service.serviceImpl.TinyUrlServiceImpl;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
public class ShortnerController {

    private static final Logger log = LoggerFactory.getLogger(ShortnerController.class);
    private TinyUrlServiceImpl tinyUrlServiceImpl;

    public ShortnerController(TinyUrlServiceImpl serviceImpl) {
        this.tinyUrlServiceImpl = serviceImpl;
    }

    @PostMapping("/tiny-url")
    public ResponseEntity<String> shortUrl(@Validated @RequestBody UrlRequest entity) {
        log.info("Request received for shortening URL: {}"+entity.url());
        String shortUrl = tinyUrlServiceImpl.generateShortUrl(entity);
        return new ResponseEntity<String>(shortUrl, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String id) {

        String originalUrl = tinyUrlServiceImpl.getUrl(id);

        if (StringUtils.isBlank(originalUrl)) {
            return ResponseEntity.notFound().build();
        }

        URI uri = URI.create(originalUrl);
        if (!List.of("http", "https").contains(uri.getScheme())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(uri)
                .build();
    }

}
