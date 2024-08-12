package com.challenge.urlshortener.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.entity.UrlEntity;
import com.challenge.urlshortener.exception.ResourceNotFoundException;
import com.challenge.urlshortener.service.UrlService;
import com.challenge.urlshortener.util.UrlShortenerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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

  private Page<UrlResponseDTO> urlEntityPage;

  private PaginatedResponseDTO<UrlResponseDTO> urlEntityPaginatedResponseDTO;

  private final int page = 0;

  private final int pageSize = 10;

  private final Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

  private final Pageable pageRequest = PageRequest.of(page, pageSize, sort);

  private List<UrlResponseDTO> urlEntityList;

  @BeforeEach
  void setup() {

    id = faker.number().randomNumber();

    originalUrl = faker.internet().url();

    shortUrl = UrlShortenerUtil.generateShortUrl();

    createdAt = LocalDateTime.now();

    urlEntity = new UrlEntity(id, originalUrl, shortUrl, createdAt);

    urlRequestDTO = new UrlRequestDTO(originalUrl);

    urlResponseDTO = new UrlResponseDTO(id, originalUrl, shortUrl, createdAt);

    urlEntityList =
        List.of(
            new UrlResponseDTO(
                faker.number().randomNumber(),
                faker.internet().url(),
                faker.random().hex(8),
                LocalDateTime.now()),
            new UrlResponseDTO(
                faker.number().randomNumber(),
                faker.internet().url(),
                faker.random().hex(8),
                LocalDateTime.now()),
            new UrlResponseDTO(
                faker.number().randomNumber(),
                faker.internet().url(),
                faker.random().hex(8),
                LocalDateTime.now()));

    urlEntityPage =
        new PageImpl<>(urlEntityList, pageRequest, urlEntityList.size());

    urlEntityPaginatedResponseDTO =
        new PaginatedResponseDTO<UrlResponseDTO>()
            .builder()
            .setContent(urlEntityList)
            .setPage(urlEntityPage.getNumber())
            .setSize(urlEntityPage.getSize())
            .setTotalElements(urlEntityPage.getTotalElements())
            .setTotalPages(urlEntityPage.getTotalPages())
            .setLast(urlEntityPage.isLast())
            .build();
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

  @DisplayName(
      "Test given Short URL when redirect to original url then should redirect"
          + " to the original url")
  @Test
  void
      testGivenShortUrl_WhenRedirectToOriginalUrl_ThenShouldRedirectToTheOriginalUrl()
          throws Exception {

    // Given / Arrange
    given(urlService.getOriginalUrl(shortUrl)).willReturn(urlResponseDTO);

    // When / Act
    ResultActions resultActions =
        mockMvc.perform(
            get("/api/v1/urls/{shortUrl}", urlResponseDTO.getShortUrl()));

    String redirectUrl =
        resultActions.andReturn().getResponse().getRedirectedUrl();

    // Then / Assert
    assertNotNull(redirectUrl);
    assertEquals(urlResponseDTO.getOriginalUrl(), redirectUrl);
  }

  @DisplayName(
      "Test given Invalid Short URL when redirect to original url then should"
          + " redirect to the original url")
  @Test
  void testGivenInvalidShortUrl_WhenRedirectToOriginalUrl_ThenReturnNotFound()
      throws Exception {

    // Given / Arrange
    given(urlService.getOriginalUrl(shortUrl))
        .willThrow(ResourceNotFoundException.class);

    // When / Act
    ResultActions resultActions =
        mockMvc.perform(
            get("/api/v1/urls/{shortUrl}", urlResponseDTO.getShortUrl()));

    MockHttpServletResponse response = resultActions.andReturn().getResponse();

    // Then / Assert
    assertNotNull(response);
    assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
  }

  @DisplayName(
      "Test given updated url when update then return url response DTO")
  @Test
  void testGivenUpdatedUrl_whenUpdate_thenReturnUrlResponseDTO()
      throws Exception {

    // Given / Arrange
    given(urlService.updateUrl(anyLong(), any(UrlRequestDTO.class)))
        .willReturn(urlResponseDTO);

    // When / Act
    MockHttpServletResponse response =
        mockMvc
            .perform(
                put("/api/v1/urls/{urlId}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(urlRequestDTO)))
            .andReturn()
            .getResponse();

    UrlResponseDTO responseDTO =
        mapper.readValue(response.getContentAsString(), UrlResponseDTO.class);

    // Then / Assert
    assertNotNull(responseDTO);
    assertEquals(response.getStatus(), HttpStatus.OK.value());
    assertEquals(urlRequestDTO.getOriginalUrl(), responseDTO.getOriginalUrl());
  }

  @DisplayName(
      "Test given pagination parameters when list urls then should return URL"
          + " list paginated")
  @Test
  void
      testGivenPaginationParametersWhenListUrlsThenShouldReturnUrlListPaginated()
          throws Exception {

    // Given / Arrange
    given(urlService.getUrlsList(any(pageRequest.getClass())))
        .willReturn(urlEntityPaginatedResponseDTO);

    // When / Act
    ResultActions resultActions = mockMvc.perform(get("/api/v1/urls"));

    String response =
        resultActions.andReturn().getResponse().getContentAsString();

    PaginatedResponseDTO<UrlResponseDTO> jsonResponse =
        mapper.readValue(response, PaginatedResponseDTO.class);

    // Then / Assert
    assertNotNull(jsonResponse);
    assertEquals(
        urlEntityPaginatedResponseDTO.getSize(), jsonResponse.getSize());
    assertEquals(
        urlEntityPaginatedResponseDTO.getContent().size(),
        urlEntityList.size());
  }
}
