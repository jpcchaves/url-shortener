
package com.challenge.urlshortener.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.factory.UrlFactory;
import com.challenge.urlshortener.repository.UrlRepository;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    private final int page = 0;
    private final int pageSize = 10;
    @Mock private UrlRepository urlRepository;
    @Mock private UrlFactory urlFactory;
    private Faker faker = new Faker();
    private UrlEntity urlEntity;
    private String originalUrl;
    private String shortUrl;
    private Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

    private final Pageable pageRequest = PageRequest.of(page, pageSize, sort);

    private Page<UrlEntity> urlEntityPage;

    private UrlRequestDTO urlRequestDTO;

    @InjectMocks private UrlServiceImpl urlService;

    @BeforeEach
    public void setUp() {

        long id = faker.number().randomNumber();
        originalUrl = faker.internet().url();
        shortUrl = UrlShortenerUtil.generateShortUrl();
        LocalDateTime createdAt = LocalDateTime.now();

        List<UrlEntity> urlEntityList =
                List.of(
                        new UrlEntity(
                                faker.number().randomNumber(), faker.internet().url(), faker.random().hex(8), LocalDateTime.now()),
                        new UrlEntity(
                                faker.number().randomNumber(), faker.internet().url(), faker.random().hex(8), LocalDateTime.now()),
                        new UrlEntity(
                                faker.number().randomNumber(), faker.internet().url(), faker.random().hex(8), LocalDateTime.now()));

        urlEntityPage = new PageImpl<>(urlEntityList, pageRequest, urlEntityList.size());

        urlRequestDTO = new UrlRequestDTO(originalUrl);

        urlEntity = new UrlEntity(id, originalUrl, shortUrl, createdAt);
    }

    @DisplayName("Test Given UrlRequestDTO When Save Should Return " + "UrlResponseDTO")
    @Test
    void testGivenUrlRequestDTO_WhenSave_ShouldReturnUrlResponseDTO() {

        // Given / Arrange
        given(urlFactory.buildUrlEntity()).willReturn(urlEntity);

        given(urlRepository.saveAndFlush(urlEntity)).willReturn(urlEntity);

        // When / Act
        UrlResponseDTO urlResponseDTO = urlService.shortenUrl(urlRequestDTO);

        // Then / Assert
        assertNotNull(urlResponseDTO);
        assertTrue(urlResponseDTO.getId() > 0);
        assertEquals(urlResponseDTO.getOriginalUrl(), originalUrl);
        assertEquals(urlResponseDTO.getShortUrl(), urlEntity.getShortUrl());
        assertNotNull(urlResponseDTO.getCreatedAt());
    }

    @DisplayName("Test given pagination pararmeters when list urls should " + "return PaginatedResponseDTO")
    @Test
    void testGivenPaginationParameters_WhenListUrls_ShouldReturnPaginatedResponseDTO() {
        // Given / Arrange
        given(urlRepository.findAll(pageRequest)).willReturn(urlEntityPage);

        // When / Act
        PaginatedResponseDTO<UrlResponseDTO> paginatedResponseDTO = urlService.getUrlsList(pageRequest);

        // Then / Assert
        assertNotNull(paginatedResponseDTO);

        assertEquals(3, paginatedResponseDTO.getTotalElements());
        assertEquals(10, urlEntityPage.getSize());
        assertEquals(3, urlEntityPage.getNumberOfElements());
        assertEquals(1, urlEntityPage.getTotalPages());
        assertEquals(0, urlEntityPage.getNumber());
        assertTrue(urlEntityPage.hasContent());
    }

    @DisplayName(
            "Test given UrlRequestDTO object when update URL Object should"
                    + " update the URL Object and return UrlResponseDTO")
    @Test
    void testGivenUrlRequestDTO_WhenUpdateUrlObject_ShouldUpdateTheUrlObjectAndReturnUrlResponseDTO() {

        // Given / Arrange
        given(urlRepository.findById(anyLong())).willReturn(Optional.of(urlEntity));

        String updatedUrl = faker.internet().url();

        urlRequestDTO = new UrlRequestDTO(updatedUrl);

        given(urlRepository.saveAndFlush(urlEntity)).willReturn(urlEntity);

        // When / Act
        UrlResponseDTO urlResponseDTO = urlService.updateUrl(urlEntity.getId(), urlRequestDTO);

        // Then / Assert
        assertNotNull(urlResponseDTO);
        assertEquals(updatedUrl, urlResponseDTO.getOriginalUrl());
        assertEquals(urlEntity.getShortUrl(), urlResponseDTO.getShortUrl());
    }
}