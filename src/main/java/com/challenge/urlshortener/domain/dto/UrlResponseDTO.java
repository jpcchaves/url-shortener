package com.challenge.urlshortener.domain.dto;

import java.util.Date;

public class UrlResponseDTO {
  private Long id;

  private String originalUrl;

  private String shortUrl;

  private Integer accessCount;

  private Date createdAt;

  public UrlResponseDTO() {
  }

  public UrlResponseDTO(
      Long id,
      String originalUrl,
      String shortUrl,
      Integer accessCount,
      Date createdAt
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
