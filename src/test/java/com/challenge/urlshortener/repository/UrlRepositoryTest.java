package com.challenge.urlshortener.repository;

import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UrlRepositoryTest {

  @Autowired
  private UrlRepository urlRepository;

  @DisplayName("Given URL Object when saved then return saved URL")
  @Test
  void testGivenUrlObject_WhenSave_thenReturnSavedUrl() {

    // Given
    String originalUrl = "http://localhost:3000";
    String shortUrl = UrlShortenerUtil.generateShortUrl();

    UrlEntity url = new UrlEntity(
        originalUrl,
        shortUrl
    );

    // When
    UrlEntity savedUrl = urlRepository.saveAndFlush(url);

    // Then
    assertNotNull(savedUrl, "Expect savedUrl not to be null");

    assertNotNull(savedUrl.getCreatedAt(), "Expect createdAt not to be null");

    assertTrue(
        savedUrl
            .getCreatedAt()
            .isBefore(LocalDateTime.now()),
        "Expect createdAt to be a date before the creation timestamp"
    );

    assertTrue(savedUrl.getId() > 0, "Expect id to be higher than 0");

    assertEquals(savedUrl.getOriginalUrl(), originalUrl, "Expect OriginalUrl " +
        "match the given OriginalUrl");

    assertEquals(savedUrl.getShortUrl(), shortUrl, "Expect OriginalUrl match " +
        "the given OriginalUrl");
  }
}