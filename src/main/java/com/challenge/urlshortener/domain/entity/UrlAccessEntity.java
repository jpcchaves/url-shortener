package com.challenge.urlshortener.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "urls_accesses")
@SequenceGenerator(
    name = "seq_url_access",
    sequenceName = "seq_url_access",
    allocationSize = 1
)
public class UrlAccessEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 7706100049740930617L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
      "seq_url_access")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "url_id",
      nullable = false,
      foreignKey = @ForeignKey(
          name = "url_fk",
          value = ConstraintMode.CONSTRAINT
      ))
  @JsonIgnore
  private UrlEntity url;
  private LocalDate accessDate;
  private Integer accessCount;

  public UrlAccessEntity() {
  }

  public UrlAccessEntity(
      Long id,
      UrlEntity url,
      LocalDate accessDate,
      Integer accessCount
  ) {
    this.id = id;
    this.url = url;
    this.accessDate = accessDate;
    this.accessCount = accessCount;
  }

  public UrlAccessEntity(
      UrlEntity url,
      LocalDate accessDate,
      Integer accessCount
  ) {
    this.url = url;
    this.accessDate = accessDate;
    this.accessCount = accessCount;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UrlEntity getUrl() {
    return url;
  }

  public void setUrl(UrlEntity url) {
    this.url = url;
  }

  public LocalDate getAccessDate() {
    return accessDate;
  }

  public void setAccessDate(LocalDate accessDate) {
    this.accessDate = accessDate;
  }

  public Integer getAccessCount() {
    return accessCount;
  }

  public void setAccessCount(Integer accessCount) {
    this.accessCount = accessCount;
  }
}
