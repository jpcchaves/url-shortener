package com.challenge.urlshortener.service;

import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlStatsDTO;

public interface UrlService {

  UrlResponseDTO shortenUrl(UrlRequestDTO requestDTO);

  UrlResponseDTO getOriginalUrl(String shortUrl);

  UrlStatsDTO getUrlStats(String shortUrl);
}
