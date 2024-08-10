package com.challenge.urlshortener.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.entity.UrlAccessEntity;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.factory.UrlFactory;
import com.challenge.urlshortener.repository.UrlAccessRepository;
import com.challenge.urlshortener.repository.UrlRepository;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import java.time.LocalDate;
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
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

  private final int page = 0;

  private final int pageSize = 10;

  private final Faker faker = new Faker();

  private final Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

  private final Pageable pageRequest = PageRequest.of(page, pageSize, sort);

  @Mock private UrlRepository urlRepository;

  @Mock private UrlFactory urlFactory;

  @Mock private UrlAccessRepository urlAccessRepository;

  @InjectMocks private UrlServiceImpl urlService;

  private UrlEntity urlEntity;

  private String originalUrl;

  private String shortUrl;

  private Page<UrlEntity> urlEntityPage;

  private UrlRequestDTO urlRequestDTO;

  private UrlAccessEntity urlAccess;

  @BeforeEach
  public void setUp() {

    long id = faker.number().randomNumber();
    originalUrl = faker.internet().url();
    shortUrl = UrlShortenerUtil.generateShortUrl();
    LocalDateTime createdAt = LocalDateTime.now();

    List<UrlEntity> urlEntityList =
        List.of(
            new UrlEntity(
                faker.number().randomNumber(),
                faker.internet().url(),
                faker.random().hex(8),
                LocalDateTime.now()),
            new UrlEntity(
                faker.number().randomNumber(),
                faker.internet().url(),
                faker.random().hex(8),
                LocalDateTime.now()),
            new UrlEntity(
                faker.number().randomNumber(),
                faker.internet().url(),
                faker.random().hex(8),
                LocalDateTime.now()));

    urlEntityPage =
        new PageImpl<>(urlEntityList, pageRequest, urlEntityList.size());

    urlRequestDTO = new UrlRequestDTO(originalUrl);

    urlEntity = new UrlEntity(id, originalUrl, shortUrl, createdAt);

    urlAccess = new UrlAccessEntity(urlEntity, LocalDate.now(), 0);
  }

  @DisplayName(
      "Test Given UrlRequestDTO When Save Should Return UrlResponseDTO")
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

  @DisplayName(
      "Test given UrlRequestDTO object when update URL Object should"
          + " update the URL Object and return UrlResponseDTO")
  @Test
  void
      testGivenUrlRequestDTO_WhenUpdateUrlObject_ShouldUpdateTheUrlObjectAndReturnUrlResponseDTO() {

    // Given / Arrange
    given(urlRepository.findById(anyLong())).willReturn(Optional.of(urlEntity));

    String updatedUrl = faker.internet().url();

    urlRequestDTO = new UrlRequestDTO(updatedUrl);

    given(urlRepository.saveAndFlush(urlEntity)).willReturn(urlEntity);

    // When / Act
    UrlResponseDTO urlResponseDTO =
        urlService.updateUrl(urlEntity.getId(), urlRequestDTO);

    // Then / Assert
    assertNotNull(urlResponseDTO);
    assertEquals(updatedUrl, urlResponseDTO.getOriginalUrl());
    assertEquals(urlEntity.getShortUrl(), urlResponseDTO.getShortUrl());
  }

  @DisplayName(
      "Test given pagination parameters when list urls should "
          + "return PaginatedResponseDTO")
  @Test
  void
      testGivenPaginationParameters_WhenListUrls_ShouldReturnPaginatedResponseDTO() {
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

  @DisplayName("Test given URL id when delete by id then should do nothing")
  @Test
  void testGivenUrlId_WhenDeleteById_ThenShouldDoNothing() {

    // Given / Arrange
    given(urlRepository.findById(anyLong())).willReturn(Optional.of(urlEntity));
    willDoNothing().given(urlRepository).deleteById(urlEntity.getId());

    // When / Act
    urlService.deleteUrl(urlEntity.getId());

    // Then / Assert
    verify(urlRepository, times(1)).findById(urlEntity.getId());
    verify(urlRepository, times(1)).deleteById(urlEntity.getId());
  }

  @DisplayName(
      "Test given Short Url when find by short url then should return"
          + " UrlResponseDTO object")
  @Test
  void
      testGivenShortUrl_WhenFindByShortUrl_ThenShouldReturnUrlResponseDTOObject() {

    // Given / Arrange
    given(urlRepository.findByShortUrl(urlEntity.getShortUrl()))
        .willReturn(Optional.of(urlEntity));

    given(urlAccessRepository.save(urlAccess)).willReturn(urlAccess);

    given(
            urlAccessRepository.findByUrlAndAccessDate(
                urlEntity.getId(), LocalDate.now()))
        .willReturn(Optional.of(urlAccess));

    // When / Act
    UrlResponseDTO response =
        urlService.getOriginalUrl(urlEntity.getShortUrl());

    // Then / Assert
    assertNotNull(response);
    assertEquals(urlEntity.getId(), response.getId());
    assertEquals(urlEntity.getOriginalUrl(), response.getOriginalUrl());
    assertEquals(urlEntity.getShortUrl(), response.getShortUrl());
    assertEquals(urlEntity.getCreatedAt(), response.getCreatedAt());
  }
}
