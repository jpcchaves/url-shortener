package com.challenge.urlshortener.util;

import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import java.util.List;

public class UrlShortenerMapper {

  public static UrlResponseDTO toDto(UrlEntity urlEntity) {
    return new UrlResponseDTO(
        urlEntity.getId(),
        urlEntity.getOriginalUrl(),
        urlEntity.getShortUrl(),
        urlEntity.getCreatedAt());
  }

  public static List<UrlResponseDTO> toDto(List<UrlEntity> urlEntityList) {
    return urlEntityList.stream().map(UrlShortenerMapper::toDto).toList();
  }
}
