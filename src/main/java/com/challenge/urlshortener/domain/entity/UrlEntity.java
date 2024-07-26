package com.challenge.urlshortener.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Table(name = "urls", indexes = {
    @Index(name = "short_url_idx", columnList = "short_url"),
    @Index(name = "created_at_idx", columnList = "created_at")
})
@Entity
@SequenceGenerator(name = "seq_url_entity", sequenceName = "seq_url_entity",
    allocationSize = 1)
public class UrlEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = -5595771988486172814L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
      "seq_url_entity")
  private Long id;

  @Column(nullable = false)
  private String originalUrl;

  @Column(nullable = false)
  private String shortUrl;

  @OneToMany(
      mappedBy = "url",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true
  )
  private List<UrlAccessEntity> accessLogs;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  public UrlEntity() {
  }

  public UrlEntity(
      Long id,
      String originalUrl,
      String shortUrl,
      List<UrlAccessEntity> accessLogs,
      LocalDateTime createdAt
  ) {
    this.id = id;
    this.originalUrl = originalUrl;
    this.shortUrl = shortUrl;
    this.accessLogs = accessLogs;
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

  public List<UrlAccessEntity> getAccessLogs() {
    return accessLogs;
  }

  public void setAccessLogs(List<UrlAccessEntity> accessLogs) {
    this.accessLogs = accessLogs;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UrlEntity urlEntity = (UrlEntity) o;
    return Objects.equals(id, urlEntity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "UrlEntity{" +
        "id=" + id +
        ", originalUrl='" + originalUrl + '\'' +
        ", shortUrl='" + shortUrl + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
