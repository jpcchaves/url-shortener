package com.challenge.urlshortener.domain.dto;

public class UrlStatsDTO {
  private Integer accessCount;
  private Double averageAccessPerDay;

  public UrlStatsDTO() {
  }

  public UrlStatsDTO(
      Integer accessCount,
      Double averageAccessPerDay
  ) {
    this.accessCount = accessCount;
    this.averageAccessPerDay = averageAccessPerDay;
  }

  public Integer getAccessCount() {
    return accessCount;
  }

  public void setAccessCount(Integer accessCount) {
    this.accessCount = accessCount;
  }

  public Double getAverageAccessPerDay() {
    return averageAccessPerDay;
  }

  public void setAverageAccessPerDay(Double averageAccessPerDay) {
    this.averageAccessPerDay = averageAccessPerDay;
  }
}
