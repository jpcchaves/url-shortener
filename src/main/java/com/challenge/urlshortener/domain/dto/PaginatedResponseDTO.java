package com.challenge.urlshortener.domain.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaginatedResponseDTO<T> implements Serializable {
  @Serial
  private static final long serialVersionUID = 4917598859451870178L;

  private List<T> content = new ArrayList<>();
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
  private boolean last;

  public PaginatedResponseDTO() {
  }

  public PaginatedResponseDTO(
      List<T> content,
      int page,
      int size,
      long totalElements,
      int totalPages,
      boolean last
  ) {
    this.content = content;
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.last = last;
  }

  public PaginatedResponseDTO(Builder<T> builder) {
    this.content = builder.content;
    this.page = builder.page;
    this.size = builder.size;
    this.totalElements = builder.totalElements;
    this.totalPages = builder.totalPages;
    this.last = builder.last;
  }

  public Builder<T> builder() {
    return new Builder<>();
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(long totalElements) {
    this.totalElements = totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public boolean isLast() {
    return last;
  }

  public void setLast(boolean last) {
    this.last = last;
  }

  public static class Builder<T> {
    private List<T> content = new ArrayList<>();
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public Builder() {
    }

    public Builder<T> setContent(List<T> content) {
      this.content = content;
      return this;
    }

    public Builder<T> setPage(int page) {
      this.page = page;
      return this;
    }

    public Builder<T> setSize(int size) {
      this.size = size;
      return this;
    }

    public Builder<T> setTotalElements(long totalElements) {
      this.totalElements = totalElements;
      return this;
    }

    public Builder<T> setTotalPages(int totalPages) {
      this.totalPages = totalPages;
      return this;
    }

    public Builder<T> setLast(boolean last) {
      this.last = last;
      return this;
    }

    public PaginatedResponseDTO<T> build() {
      return new PaginatedResponseDTO<T>(this);
    }
  }
}
