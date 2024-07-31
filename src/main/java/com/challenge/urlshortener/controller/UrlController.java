package com.challenge.urlshortener.controller;

import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlStatsDTO;
import com.challenge.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Url Controller")
@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

  private final UrlService urlService;

  public UrlController(UrlService urlService) {
    this.urlService = urlService;
  }

  @Operation(
      summary = "Creates a shortened URL",
      description =
          "Creates a shortened URL by passing the original URL in the request" +
              " body",
      responses = {
          @ApiResponse(
              description = "Created",
              responseCode = "201",
              content =
              @Content(
                  schema =
                  @Schema(implementation = UrlResponseDTO.class))),
          @ApiResponse(
              description = "Bad Request",
              responseCode = "400",
              content = @Content)
      })
  @PostMapping
  public ResponseEntity<UrlResponseDTO> createShortUrl(
      @RequestBody @Valid
      UrlRequestDTO requestDTO
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(urlService.shortenUrl(requestDTO));
  }

  @Operation(
      summary = "Updates a URL",
      description =
          "Updates a URL URL by passing the urlId and the originalUrl in JSON" +
              " request body",
      responses = {
          @ApiResponse(
              description = "Updated",
              responseCode = "200",
              content =
              @Content(
                  schema =
                  @Schema(implementation = UrlResponseDTO.class))),
          @ApiResponse(
              description = "Bad Request",
              responseCode = "400",
              content = @Content)
      })
  @PutMapping("/{urlId}")
  public ResponseEntity<UrlResponseDTO> updateShortUrl(
      @PathVariable(name = "urlId") Long urlId,
      @RequestBody UrlRequestDTO requestDTO
  ) {
    return ResponseEntity.ok(urlService.updateUrl(urlId, requestDTO));
  }

  @Operation(
      summary = "Deletes a URL",
      description = "Deletes a URL by passing the urlId as a path variable.",
      responses = {
          @ApiResponse(
              description = "No Content",
              responseCode = "204",
              content = @Content),
          @ApiResponse(
              description = "Not Found",
              responseCode = "404",
              content = @Content)
      })
  @DeleteMapping("/{urlId}")
  public ResponseEntity<Void> deleteUrl(@PathVariable(name = "urlId") Long urlId) {

    urlService.deleteUrl(urlId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(
      summary = "Access a url",
      description =
          "Access the original URL by passing the shortened URL",
      responses = {
          @ApiResponse(
              description = "Success",
              responseCode = "200",
              content = @Content
          ),
          @ApiResponse(
              description = "Bad Request",
              responseCode = "400",
              content = @Content)
      })
  @GetMapping("/{shortUrl}")
  public void redirectUrl(
      @PathVariable(name = "shortUrl") String shortUrl,
      HttpServletResponse response
  ) throws IOException {

    UrlResponseDTO responseDTO = urlService.getOriginalUrl(shortUrl);

    response.sendRedirect(responseDTO.getOriginalUrl());

  }

  @Operation(
      summary = "Get URL stats",
      description =
          "Get the stats about an URL, like its number of accesses and " +
              "average access per day since it was created",
      responses = {
          @ApiResponse(
              description = "Success",
              responseCode = "200",
              content =
              @Content(
                  schema =
                  @Schema(implementation = UrlStatsDTO.class))),
          @ApiResponse(
              description = "Bad Request",
              responseCode = "400",
              content = @Content)
      })
  @GetMapping("/{shortUrl}/stats")
  public ResponseEntity<UrlStatsDTO> getUrlStats(
      @PathVariable(name =
          "shortUrl") String shortUrl
  ) {

    return ResponseEntity.ok(urlService.getUrlStats(shortUrl));
  }


  @Operation(
      summary = "Get URL list paginated",
      description =
          "Get a list of all the URL created with pagination feature",
      responses = {
          @ApiResponse(
              description = "Success",
              responseCode = "200",
              content =
              @Content(
                  schema =
                  @Schema(implementation = UrlStatsDTO.class))),
          @ApiResponse(
              description = "Bad Request",
              responseCode = "400",
              content = @Content)
      })
  @PageableAsQueryParam
  @GetMapping
  public ResponseEntity<PaginatedResponseDTO<UrlResponseDTO>> getUrlList(@Parameter(hidden = true) Pageable pageable) {

    return ResponseEntity.ok(urlService.getUrlsList(pageable));
  }
}
