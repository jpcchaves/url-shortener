package com.challenge.urlshortener.repository;

import com.challenge.urlshortener.domain.entity.UrlAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlAccessRepository extends JpaRepository<UrlAccessEntity,
    Long> {

  @Query(value = "select * from urls_accesses u where u.url = ?1",
      nativeQuery = true)
  List<UrlAccessEntity> findByUrl(String url);

  @Query(value = "SELECT * FROM urls_accesses WHERE url_id = :urlId AND " +
      "access_date = :accessDate LIMIT 1", nativeQuery = true)
  Optional<UrlAccessEntity> findByUrlAndAccessDate(
      @Param("urlId") Long urlId,
      @Param("accessDate") LocalDate accessDate
  );
}
