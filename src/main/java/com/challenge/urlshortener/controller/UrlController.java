package com.challenge.urlshortener.controller;

import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlStatsDTO;
import com.challenge.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

  private final UrlService urlService;

  public UrlController(UrlService urlService) {
    this.urlService = urlService;
  }

  @PostMapping
  public ResponseEntity<UrlResponseDTO> createShortUrl(
      @RequestBody @Valid
      UrlRequestDTO requestDTO
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(urlService.shortenUrl(requestDTO));
  }

  @GetMapping("/{shortUrl}")
  public void redirectUrl(
      @PathVariable(name = "shortUrl") String shortUrl,
      HttpServletResponse response
  ) throws IOException {

    UrlResponseDTO responseDTO = urlService.getOriginalUrl(shortUrl);

    response.sendRedirect(responseDTO.getOriginalUrl());

  }

  @GetMapping("/{shortUrl}/stats")
  public ResponseEntity<UrlStatsDTO> getUrlStats(@PathVariable(name = "shortUrl") String shortUrl) {
    return ResponseEntity.ok(urlService.getUrlStats(shortUrl));
  }

}
