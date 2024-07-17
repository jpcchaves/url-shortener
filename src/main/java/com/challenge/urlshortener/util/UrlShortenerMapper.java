package com.challenge.urlshortener.util;

import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;

public class UrlShortenerMapper {

  public static UrlResponseDTO toDto(UrlEntity urlEntity) {
    return new UrlResponseDTO(
        urlEntity.getId(),
        urlEntity.getOriginalUrl(),
        urlEntity.getShortUrl(),
        urlEntity.getAccessCount(),
        urlEntity.getCreatedAt()
    );
  }
}
