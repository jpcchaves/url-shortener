package com.challenge.urlshortener.repository;

import com.challenge.urlshortener.domain.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {

  @Query(value = "SELECT * FROM urls WHERE short_url = :shortUrl LIMIT 1", nativeQuery = true)
  Optional<UrlEntity> findByShortUrl(String shortUrl);
}
