package com.challenge.urlshortener.service.impl;

import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlStatsDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.factory.UrlFactory;
import com.challenge.urlshortener.repository.UrlRepository;
import com.challenge.urlshortener.service.UrlService;
import com.challenge.urlshortener.util.UrlShortenerMapper;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {

  private final UrlRepository urlRepository;
  private final UrlFactory urlFactory;

  public UrlServiceImpl(
      UrlRepository urlRepository,
      UrlFactory urlFactory
  ) {
    this.urlRepository = urlRepository;
    this.urlFactory = urlFactory;
  }

  @Override
  @Transactional
  public UrlResponseDTO shortenUrl(UrlRequestDTO requestDTO) {

    String shortUrl = UrlShortenerUtil.generateShortUrl();

    UrlEntity url = urlFactory.buildUrlEntity();

    url.setOriginalUrl(requestDTO.getOriginalUrl());
    url.setShortUrl(shortUrl);

    UrlEntity savedUrl = urlRepository.save(url);

    return UrlShortenerMapper.toDto(savedUrl);
  }

  @Override
  @Transactional(readOnly = true)
  public UrlResponseDTO getOriginalUrl(String shortUrl) {

    UrlEntity url = urlRepository
        .findByShortUrl(shortUrl)
        .orElseThrow(
            () -> new RuntimeException("Url not found with the given short url: " + shortUrl));

    url.setAccessCount(url.getAccessCount() + 1);

    urlRepository.save(url);

    return UrlShortenerMapper.toDto(url);
  }

  @Override
  public UrlStatsDTO getUrlStats(String shortUrl) {

    UrlEntity url = urlRepository.findByShortUrl(shortUrl)
                                 .orElseThrow(
                                     () -> new RuntimeException("Url not found with the given short url: " + shortUrl));

    LocalDateTime now = LocalDateTime.now();

    Duration duration = Duration.between(url.getCreatedAt(), now);

    long daysBetween = Math.max(duration.toDays(), 1);

    double averageAccessesPerDay = (double) url.getAccessCount() / daysBetween;

    return new UrlStatsDTO(url.getAccessCount(), averageAccessesPerDay);
  }
}
