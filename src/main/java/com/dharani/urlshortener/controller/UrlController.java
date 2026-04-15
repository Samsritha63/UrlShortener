package com.dharani.urlshortener.controller;

import com.dharani.urlshortener.dto.UrlRequest;
import com.dharani.urlshortener.dto.UrlResponse;
import com.dharani.urlshortener.model.UrlMapping;
import com.dharani.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UrlController {

    @Value("${app.base-url}")
    private String baseUrl;

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest request) {

        String shortCode = urlService.shortenUrl(request.getOriginalUrl());

        String shortUrl = baseUrl + "/" + shortCode;

        return ResponseEntity.ok(new UrlResponse(shortUrl));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {

        Optional<UrlMapping> mapping = urlService.getOriginalUrl(shortCode);

        if (mapping.isPresent()) {
            return ResponseEntity
                    .status(302)
                    .location(URI.create(mapping.get().getOriginalUrl()))
                    .build();
        }

        return ResponseEntity.notFound().build();
    }
}