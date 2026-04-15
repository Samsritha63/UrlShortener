package com.dharani.urlshortener.service;

import com.dharani.urlshortener.model.UrlMapping;
import com.dharani.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.isBlank()) {
            throw new RuntimeException("URL cannot be empty");
        }
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }
        String shortCode = generateShortCode();

        UrlMapping mapping = new UrlMapping(
                originalUrl,
                shortCode,
                LocalDateTime.now()
        );

        urlRepository.save(mapping);

        return shortCode;
    }

    public Optional<UrlMapping> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}