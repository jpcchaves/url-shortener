package com.challenge.urlshortener.domain.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class UrlRequestDTO {
  @URL(message = "Invalid URL!")
  @NotBlank(message = "The URL must not be null or blank!")
  private String originalUrl;

  public UrlRequestDTO() {}

  public UrlRequestDTO(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }
}
