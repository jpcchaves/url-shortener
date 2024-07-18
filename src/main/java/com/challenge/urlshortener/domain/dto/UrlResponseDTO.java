package com.challenge.urlshortener.domain.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UrlResponseDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 3456859418009760272L;

  private Long id;

  private String originalUrl;

  private String shortUrl;

  private Integer accessCount;

  private LocalDateTime createdAt;

  public UrlResponseDTO() {
  }

  public UrlResponseDTO(
      Long id,
      String originalUrl,
      String shortUrl,
      Integer accessCount,
      LocalDateTime createdAt
  ) {
    this.id = id;
    this.originalUrl = originalUrl;
    this.shortUrl = shortUrl;
    this.accessCount = accessCount;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public Integer getAccessCount() {
    return accessCount;
  }

  public void setAccessCount(Integer accessCount) {
    this.accessCount = accessCount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
