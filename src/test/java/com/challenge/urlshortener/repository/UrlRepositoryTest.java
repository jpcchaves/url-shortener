package com.challenge.urlshortener.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest
class UrlRepositoryTest {

  @Autowired private UrlRepository urlRepository;

  private String originalUrl;
  private String shortUrl;

  @BeforeEach
  void setup() {

    originalUrl = "http://localhost:3000";
    shortUrl = UrlShortenerUtil.generateShortUrl();
  }

  @DisplayName("Given URL Object when saved then return saved URL")
  @Test
  void testGivenUrlObject_WhenSave_thenReturnSavedUrl() {

    // Given
    UrlEntity url = new UrlEntity(originalUrl, shortUrl);

    // When
    UrlEntity savedUrl = urlRepository.saveAndFlush(url);

    // Then
    assertNotNull(savedUrl, "Expect savedUrl not to be null");

    assertNotNull(savedUrl.getCreatedAt(), "Expect createdAt not to be null");

    assertTrue(
        savedUrl.getCreatedAt().isBefore(LocalDateTime.now()),
        "Expect createdAt to be a date before the creation timestamp");

    assertTrue(savedUrl.getId() > 0, "Expect id to be higher than 0");

    assertEquals(
        savedUrl.getOriginalUrl(),
        originalUrl,
        "Expect OriginalUrl " + "match the given OriginalUrl");

    assertEquals(
        savedUrl.getShortUrl(),
        shortUrl,
        "Expect OriginalUrl match " + "the given OriginalUrl");
  }

  @DisplayName(
      "test Given Url List Paginated When Find All Then Return Url "
          + "List Paginated")
  @Test
  void testGivenUrlListPaginated_WhenFindAll_ThenReturnUrlListPaginated() {

    // Given
    int pageNumber = 0;
    int pageSize = 20;
    Sort sort = Sort.by(Sort.DEFAULT_DIRECTION, "id");

    UrlEntity url = new UrlEntity(originalUrl, shortUrl);

    UrlEntity url2 = new UrlEntity(originalUrl, shortUrl);

    List<UrlEntity> mockedEntities = List.of(url, url2);

    urlRepository.saveAll(mockedEntities);

    // When
    Page<UrlEntity> urlEntitiesPage =
        urlRepository.findAll(PageRequest.of(pageNumber, pageSize, sort));

    // Then
    assertNotNull(urlEntitiesPage);

    assertEquals(urlEntitiesPage.getContent().size(), mockedEntities.size());

    assertEquals(pageNumber, urlEntitiesPage.getNumber());

    assertEquals(pageSize, urlEntitiesPage.getSize());

    assertEquals(urlEntitiesPage.getSort(), sort);

    assertEquals(1, urlEntitiesPage.getTotalPages());

    assertTrue(urlEntitiesPage.isLast());
  }

  @DisplayName(
      "test Given Short Url When Find By Short Url Then Return Url "
          + "Object Or Exception")
  @Test
  void testGivenShortUrl_WhenFindByShortUrl_ThenReturnUrlObjectOrException() {

    // Given
    UrlEntity url = new UrlEntity(originalUrl, shortUrl);

    urlRepository.save(url);

    // When
    UrlEntity fetchedUrl = urlRepository.findByShortUrl(shortUrl).get();

    // Then
    assertNotNull(fetchedUrl);

    assertNotNull(fetchedUrl.getCreatedAt(), "Expect createdAt not to be null");

    assertEquals(
        fetchedUrl.getOriginalUrl(),
        originalUrl,
        "Expect " + "OriginalUrl " + "match the given OriginalUrl");

    assertEquals(
        fetchedUrl.getShortUrl(),
        shortUrl,
        "Expect OriginalUrl " + "match " + "the given OriginalUrl");
  }

  @DisplayName(
      "Test given Url when updateUrl should return updated " + "Url object")
  @Test
  void testGivenUrlObjectWhenUpdateUrlShouldReturnUpdatedUrlObject() {

    // Given
    String updatedOriginalUrl = "https://localhost:3333";

    UrlEntity url = new UrlEntity(originalUrl, shortUrl);

    UrlEntity savedUrl = urlRepository.save(url);

    // When
    savedUrl.setOriginalUrl(updatedOriginalUrl);

    UrlEntity updatedUrl = urlRepository.save(url);

    // Then
    assertNotNull(updatedUrl);

    assertNotEquals(updatedUrl.getOriginalUrl(), originalUrl);

    assertEquals(updatedUrl.getOriginalUrl(), updatedOriginalUrl);
  }

  @DisplayName("Test given urlId when deleteUrl should return empty optional")
  @Test
  void testGivenUrlIdWhenDeleteUrlShouldReturnEmptyOptional() {

    // Given
    UrlEntity url = new UrlEntity(originalUrl, shortUrl);

    UrlEntity savedUrl = urlRepository.saveAndFlush(url);

    // When
    urlRepository.deleteById(savedUrl.getId());

    Optional<UrlEntity> optionalUrlEntity =
        urlRepository.findById(savedUrl.getId());

    // Then
    assertTrue(optionalUrlEntity.isEmpty());
  }
}
