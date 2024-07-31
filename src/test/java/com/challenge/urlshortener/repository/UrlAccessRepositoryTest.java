package com.challenge.urlshortener.repository;

import com.challenge.urlshortener.domain.entity.UrlAccessEntity;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UrlAccessRepositoryTest {

  @Autowired
  private UrlRepository urlRepository;
  @Autowired
  private UrlAccessRepository urlAccessRepository;
  private UrlEntity url;
  private String originalUrl;
  private String shortUrl;

  @BeforeEach
  void setup() {
    originalUrl = "http://localhost:3000";
    shortUrl = UrlShortenerUtil.generateShortUrl();

    url = urlRepository.saveAndFlush(new UrlEntity(
        originalUrl,
        shortUrl
    ));
  }

  @DisplayName("test Given Url Access Object When Save then Return Url Access" +
      " Object")
  @Test
  void testGivenUrlAccessObject_WhenSave_thenReturnUrlAccessObject() {

    // Given
    UrlAccessEntity urlAccessEntity = new UrlAccessEntity();

    Integer prevAccessCount = urlAccessEntity.getAccessCount();
    Integer updatedAccessCount = prevAccessCount + 1;

    urlAccessEntity.setAccessCount(updatedAccessCount);
    urlAccessEntity.setUrl(url);
    urlAccessEntity.setAccessDate(LocalDate.now());

    // When
    UrlAccessEntity savedUrlAccess =
        urlAccessRepository.saveAndFlush(urlAccessEntity);

    // Then
    assertNotNull(savedUrlAccess);

    assertNotNull(savedUrlAccess.getAccessDate());

    assertEquals(savedUrlAccess.getUrl(), url);

    assertTrue(updatedAccessCount > prevAccessCount);
    
    assertEquals(updatedAccessCount, savedUrlAccess.getAccessCount());
  }
}