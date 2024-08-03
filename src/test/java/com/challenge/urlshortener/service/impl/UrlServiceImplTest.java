package com.challenge.urlshortener.service.impl;

import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
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
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    private Pageable pageRequest;

    int page;

    int pageSize;

    private Sort sort;

    private Page<UrlEntity> urlEntityPage;

    private UrlRequestDTO urlRequestDTO;

    @InjectMocks
    private UrlServiceImpl urlService;

    @BeforeEach
    public void setUp() {

        long id = new Date().getTime();
        originalUrl = "https://www.google.com";
        shortUrl = UrlShortenerUtil.generateShortUrl();
        LocalDateTime createdAt = LocalDateTime.now();

        sort = Sort.by(Sort.Direction.ASC, "createdAt");
        page = 0;
        pageSize = 10;

        pageRequest = PageRequest.of(page, pageSize, sort);

        List<UrlEntity> urlEntityList = List.of(
                new UrlEntity(1L, "https://test.com/", "short1",
                        LocalDateTime.now()),
                new UrlEntity(2L, "https://test.com/", "short2",
                        LocalDateTime.now()),
                new UrlEntity(3L, "https://test.com/", "short3",
                        LocalDateTime.now())
        );

        urlEntityPage = new PageImpl<>(urlEntityList, pageRequest,
                urlEntityList.size());

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

    @DisplayName("Test given pagination pararmeters when list urls should " +
            "return PaginatedResponseDTO")
    @Test
    void testGivenPaginationParameters_WhenListUrls_ShouldReturnPaginatedResponseDTO () {
        // Given / Arrange
        given(urlRepository.findAll(pageRequest)).willReturn(urlEntityPage);

        // When / Act
        PaginatedResponseDTO<UrlResponseDTO> paginatedResponseDTO =
                urlService.getUrlsList(pageRequest);

        // Then / Assert
        assertNotNull(paginatedResponseDTO);

        assertEquals(3, paginatedResponseDTO.getTotalElements());
        assertEquals(10, urlEntityPage.getSize());
        assertEquals(3, urlEntityPage.getNumberOfElements());
        assertEquals(1, urlEntityPage.getTotalPages());
        assertEquals(0, urlEntityPage.getNumber());
        assertTrue(urlEntityPage.hasContent());
    }

}