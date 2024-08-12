package com.challenge.urlshortener.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UrlShortenerUtilTest {

  @DisplayName(
      "Test given short url when generate method is called should "
          + "return short url")
  @Test
  void testGivenShortUrlWhenGenerateMethodIsCalledShouldReturnShortUrl() {

    // Given
    int shortUrlExpectedLength = 8;

    // When
    String shortUrl = UrlShortenerUtil.generateShortUrl();

    // Then
    assertNotNull(shortUrl);

    assertEquals(shortUrlExpectedLength, shortUrl.length());
  }
}
