package com.challenge.urlshortener.service.impl;

import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlStatsDTO;
import com.challenge.urlshortener.domain.entity.UrlAccessEntity;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.exception.ExceptionDefinition;
import com.challenge.urlshortener.exception.ResourceNotFoundException;
import com.challenge.urlshortener.factory.UrlFactory;
import com.challenge.urlshortener.repository.UrlAccessRepository;
import com.challenge.urlshortener.repository.UrlRepository;
import com.challenge.urlshortener.service.UrlService;
import com.challenge.urlshortener.util.UrlShortenerMapper;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {

  private final UrlRepository urlRepository;
  private final UrlAccessRepository urlAccessRepository;
  private final UrlFactory urlFactory;

  public UrlServiceImpl(
      UrlRepository urlRepository,
      UrlAccessRepository urlAccessRepository,
      UrlFactory urlFactory
  ) {
    this.urlRepository = urlRepository;
    this.urlAccessRepository = urlAccessRepository;
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
  public UrlResponseDTO updateUrl(
      Long urlId,
      UrlRequestDTO requestDTO
  ) {

    UrlEntity urlEntity = urlRepository
        .findById(urlId)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionDefinition.URL0002));

    urlEntity.setOriginalUrl(requestDTO.getOriginalUrl());

    urlEntity = urlRepository.saveAndFlush(urlEntity);

    return UrlShortenerMapper.toDto(urlEntity);
  }

  @Override
  public void deleteUrl(Long urlId) {

    UrlEntity urlEntity = urlRepository
        .findById(urlId)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionDefinition.URL0002));

    urlRepository.deleteById(urlEntity.getId());
  }

  @Override
  @Transactional
  public UrlResponseDTO getOriginalUrl(String shortUrl) {

    UrlEntity url = urlRepository
        .findByShortUrl(shortUrl)
        .orElseThrow(
            () -> new ResourceNotFoundException(ExceptionDefinition.URL0001));

    recordAccess(url);

    return UrlShortenerMapper.toDto(url);
  }

  @Override
  public UrlStatsDTO getUrlStats(String shortUrl) {
    UrlEntity url = urlRepository.findByShortUrl(shortUrl)
                                 .orElseThrow(
                                     () -> new ResourceNotFoundException(ExceptionDefinition.URL0001));

    return new UrlStatsDTO(
        url.getOriginalUrl(),
        url.getShortUrl(),
        url.getAccessLogs()
    );
  }

  @Override
  public PaginatedResponseDTO<UrlResponseDTO> getUrlsList(Pageable pageable) {

    Page<UrlEntity> urlEntityList = urlRepository.findAll(pageable);

    List<UrlResponseDTO> urlResponseDTOList =
        UrlShortenerMapper.toDto(urlEntityList.getContent());

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

  private void recordAccess(UrlEntity url) {

    LocalDate today = LocalDate.now();

    Optional<UrlAccessEntity> urlAccessOptional =
        urlAccessRepository.findByUrlAndAccessDate(url.getId(), today);

    UrlAccessEntity urlAccess;

    if (urlAccessOptional.isPresent()) {

      urlAccess = urlAccessOptional.get();

      urlAccess.setAccessCount(urlAccess.getAccessCount() + 1);

    } else {

      urlAccess = new UrlAccessEntity(url, today, 1);
    }

    urlAccessRepository.save(urlAccess);
  }
}
