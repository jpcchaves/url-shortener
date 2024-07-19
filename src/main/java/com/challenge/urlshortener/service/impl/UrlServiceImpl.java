package com.challenge.urlshortener.service.impl;

import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlStatsDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.exception.ExceptionDefinition;
import com.challenge.urlshortener.exception.ResourceNotFoundException;
import com.challenge.urlshortener.factory.UrlFactory;
import com.challenge.urlshortener.repository.UrlRepository;
import com.challenge.urlshortener.service.UrlService;
import com.challenge.urlshortener.util.UrlShortenerMapper;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    UrlEntity savedUrl = urlRepository.saveAndFlush(url);

    return UrlShortenerMapper.toDto(savedUrl);
  }

  @Override
  @Transactional
  public UrlResponseDTO getOriginalUrl(String shortUrl) {

    UrlEntity url = urlRepository
        .findByShortUrl(shortUrl)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionDefinition.URL0001));

    url.setAccessCount(url.getAccessCount() + 1);

    urlRepository.save(url);

    return UrlShortenerMapper.toDto(url);
  }

  @Override
  public UrlStatsDTO getUrlStats(String shortUrl) {

    UrlEntity url = urlRepository.findByShortUrl(shortUrl)
                                 .orElseThrow(
                                     () -> new ResourceNotFoundException(ExceptionDefinition.URL0001));

    LocalDateTime now = LocalDateTime.now();

    Duration duration = Duration.between(url.getCreatedAt(), now);

    long daysBetween = Math.max(duration.toDays(), 1);

    double averageAccessesPerDay = (double) url.getAccessCount() / daysBetween;

    return new UrlStatsDTO(url.getAccessCount(), averageAccessesPerDay);
  }

  @Override
  public PaginatedResponseDTO<UrlResponseDTO> getUrlsList(Pageable pageable) {

    Page<UrlEntity> urlEntityList = urlRepository.findAll(pageable);

    List<UrlResponseDTO> urlResponseDTOList = UrlShortenerMapper.toDto(urlEntityList.getContent());

    return new PaginatedResponseDTO<UrlResponseDTO>()
        .builder()
        .setContent(urlResponseDTOList)
        .setPage(urlEntityList.getNumber())
        .setSize(urlEntityList.getSize())
        .setTotalElements(urlEntityList.getTotalElements())
        .setTotalPages(urlEntityList.getTotalPages())
        .setLast(urlEntityList.isLast())
        .build();
  }
}
