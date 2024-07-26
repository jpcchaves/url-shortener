package com.challenge.urlshortener.domain.dto;

import com.challenge.urlshortener.domain.entity.UrlAccessEntity;

import java.util.ArrayList;
import java.util.List;

public class UrlStatsDTO {
  private String originalUrl;
  private String shortenedUrl;
  private List<UrlAccessEntity> accessLogs = new ArrayList<>();

  public UrlStatsDTO() {
  }

  public UrlStatsDTO(
      String originalUrl,
      String shortenedUrl,
      List<UrlAccessEntity> accessLogs
  ) {
    this.originalUrl = originalUrl;
    this.shortenedUrl = shortenedUrl;
    this.accessLogs = accessLogs;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getShortenedUrl() {
    return shortenedUrl;
  }

  public void setShortenedUrl(String shortenedUrl) {
    this.shortenedUrl = shortenedUrl;
  }

  public List<UrlAccessEntity> getAccessLogs() {
    return accessLogs;
  }

  public void setAccessLogs(List<UrlAccessEntity> accessLogs) {
    this.accessLogs = accessLogs;
  }
}
