package com.challenge.urlshortener.service.impl;

import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.factory.UrlFactory;
import com.challenge.urlshortener.repository.UrlRepository;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UrlFactory urlFactory;

    private UrlEntity urlEntity;

    private String originalUrl;
    private String shortUrl;
    private UrlRequestDTO urlRequestDTO;

    @InjectMocks
    private UrlServiceImpl urlService;

    @BeforeEach
    public void setUp() {

        long id = new Date().getTime();
        originalUrl = "https://www.google.com";
        shortUrl = UrlShortenerUtil.generateShortUrl();
        LocalDateTime createdAt = LocalDateTime.now();

        urlRequestDTO = new UrlRequestDTO(originalUrl);

        urlEntity = new UrlEntity(
                id,
                originalUrl,
                shortUrl,
                createdAt
        );
    }

    @DisplayName("Test Given UrlRequestDTO When Save Should Return " +
            "UrlResponseDTO")
    @Test
    void testGivenUrlRequestDTO_WhenSave_ShouldReturnUrlResponseDTO() {

        // Given / Arrange
        given(urlFactory.buildUrlEntity())
                .willReturn(urlEntity);

        given(urlRepository.saveAndFlush(urlEntity))
                .willReturn(urlEntity);

        // When / Act
        UrlResponseDTO urlResponseDTO = urlService.shortenUrl(urlRequestDTO);

        // Then / Assert
        assertNotNull(urlResponseDTO);
        assertTrue(urlResponseDTO.getId() > 0);
        assertEquals(urlResponseDTO.getOriginalUrl(), originalUrl);
        assertEquals(urlResponseDTO.getShortUrl(), urlEntity.getShortUrl());
        assertNotNull(urlResponseDTO.getCreatedAt());
    }

}