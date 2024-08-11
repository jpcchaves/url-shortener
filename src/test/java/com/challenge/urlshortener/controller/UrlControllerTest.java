package com.challenge.urlshortener.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.service.UrlService;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest
public class UrlControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @MockBean private UrlService urlService;

  private final Faker faker = new Faker();

  private UrlEntity urlEntity;

  private UrlRequestDTO urlRequestDTO;

  private UrlResponseDTO urlResponseDTO;

  Long id;

  private String originalUrl;

  private String shortUrl;

  private LocalDateTime createdAt;

  @BeforeEach
  void setup() {

    id = faker.number().randomNumber();

    originalUrl = faker.internet().url();

    shortUrl = UrlShortenerUtil.generateShortUrl();

    createdAt = LocalDateTime.now();

    urlEntity = new UrlEntity(id, originalUrl, shortUrl, createdAt);

    urlRequestDTO = new UrlRequestDTO(originalUrl);

    urlResponseDTO = new UrlResponseDTO(id, originalUrl, shortUrl, createdAt);
  }

  @DisplayName(
      "Test given UrlRequestDTO when shorten url then return UrlResponseDTO")
  @Test
  void testGivenUrlRequestDTO_WhenShortenUrl_thenShouldReturnUrlResponseDTO()
      throws Exception {

    // Given / Arrange
    given(urlService.shortenUrl(any(UrlRequestDTO.class)))
        .willReturn(urlResponseDTO);

    // When / Act
    ResultActions resultActions =
        mockMvc.perform(
            post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urlRequestDTO)));

    // Then / Assert
    MvcResult response = resultActions.andReturn();

    String jsonResponse = response.getResponse().getContentAsString();
    UrlResponseDTO responseDTO =
        mapper.readValue(jsonResponse, UrlResponseDTO.class);

    // JUnit assertions
    assertEquals(
        response.getResponse().getStatus(), HttpStatus.CREATED.value());
    assertEquals(id, responseDTO.getId());
    assertTrue(id > 0);
    assertEquals(urlRequestDTO.getOriginalUrl(), responseDTO.getOriginalUrl());
    assertEquals(responseDTO.getShortUrl(), shortUrl);
    assertEquals(responseDTO.getCreatedAt(), createdAt);

    /* Hamcrest
    *
    * From my point of view, the hamcrest approach may lead to an uncontrolled amount of method chaining,
    * which may lead to an read to understand and hard to maintain test code,
    * I'm leaving it here, so in the future, if needed, I can know how a hamcrest WebMvcTest looks like
    *


        response
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.id").value(greaterThan(0)))
            .andExpect(
                jsonPath("$.originalUrl", is(urlRequestDTO.getOriginalUrl())))
            .andExpect(jsonPath("$.shortUrl", notNullValue()))
            .andExpect(jsonPath("$.shortUrl", is(shortUrl)))
            .andExpect(jsonPath("$.createdAt", notNullValue()));

    */
  }
}
