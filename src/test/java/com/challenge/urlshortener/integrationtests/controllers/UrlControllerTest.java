package com.challenge.urlshortener.integrationtests.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.challenge.urlshortener.config.SingletonObjectMapperConfig;
import com.challenge.urlshortener.config.TestConfigs;
import com.challenge.urlshortener.domain.dto.PaginatedResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.domain.dto.UrlStatsDTO;
import com.challenge.urlshortener.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UrlControllerTest extends AbstractIntegrationTest {

  private static RequestSpecification requestSpecification;
  private static ObjectMapper mapper;
  private static UrlRequestDTO urlRequestDTO;
  private static UrlResponseDTO urlResponseDTO;
  private static Faker faker;

  @BeforeAll
  public static void setup() {

    faker = new Faker();

    mapper = SingletonObjectMapperConfig.getInstance();

    requestSpecification =
        new RequestSpecBuilder()
            .setBasePath("/api/v1/urls")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

    urlRequestDTO = new UrlRequestDTO(faker.internet().url());
  }

  @DisplayName(
      "Integration test given url request dto when shorten a url then should"
          + " return url response dto")
  @Test
  @Order(1)
  void
      integrationTestGivenUrlRequestDTO_WhenShorteningAUrl_ThenShouldReturnUrlResponseDTO()
          throws IOException {

    String requestBody =
        given()
            .spec(requestSpecification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(urlRequestDTO)
            .when()
            .post()
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .asString();

    urlResponseDTO = mapper.readValue(requestBody, UrlResponseDTO.class);

    assertNotNull(urlResponseDTO);
    assertNotNull(urlResponseDTO.getId());
    assertNotNull(urlResponseDTO.getOriginalUrl());
    assertNotNull(urlResponseDTO.getShortUrl());
    assertNotNull(urlResponseDTO.getCreatedAt());

    assertTrue(urlResponseDTO.getId() > 0);

    assertEquals(
        urlRequestDTO.getOriginalUrl(), urlResponseDTO.getOriginalUrl());
  }

  @DisplayName(
      "Integration test given UrlRequestDTO when update url then should return"
          + " updated UrlResponseDTO")
  @Test
  @Order(2)
  void
      integrationTestGivenUrlRequestDTO_WhenUpdateURL_ThenShouldReturnUpdatedUrlResponseDTO()
          throws IOException {

    urlRequestDTO = new UrlRequestDTO(faker.internet().url());

    String requestBody =
        given()
            .spec(requestSpecification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(urlRequestDTO)
            .when()
            .put("/{urlId}", urlResponseDTO.getId())
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .asString();

    urlResponseDTO = mapper.readValue(requestBody, UrlResponseDTO.class);

    assertNotNull(requestBody);

    assertNotNull(urlResponseDTO.getId());
    assertNotNull(urlResponseDTO.getOriginalUrl());
    assertNotNull(urlResponseDTO.getShortUrl());
    assertNotNull(urlResponseDTO.getCreatedAt());

    assertTrue(urlResponseDTO.getId() > 0);

    assertEquals(
        urlRequestDTO.getOriginalUrl(), urlResponseDTO.getOriginalUrl());
  }

  @DisplayName(
      "Integration test given shortUrl when find by shortUrl  then should"
          + " return UrlResponseDTO")
  @Test
  @Order(3)
  void testGivenShortUrl_WhenFindByShortUrl_ThenShouldReturnUrlResponseDTO() {

    ResponseOptions<?> response =
        given()
            .spec(requestSpecification)
            .redirects()
            .follow(false)
            .when()
            .get("/{shortUrl}", urlResponseDTO.getShortUrl())
            .andReturn();

    assertEquals(response.statusCode(), HttpStatus.FOUND.value());
    assertEquals(urlResponseDTO.getOriginalUrl(), response.header("Location"));
  }

  @DisplayName(
      "Integration test given shortUrl when get url stats then should return"
          + " UrlStatsDTO")
  @Test
  @Order(4)
  void
      integrationTestGivenShortUrl_WhenGetUrlStats_ThenShouldReturnUrlStatsDTO()
          throws IOException {

    // Given / Arrange
    ResponseOptions<?> response =
        given()
            .spec(requestSpecification)
            .when()
            .get("/{shortUrl}/stats", urlResponseDTO.getShortUrl())
            .andReturn();

    // When / Act
    UrlStatsDTO statsDTO =
        mapper.readValue(response.body().asString(), UrlStatsDTO.class);

    // Then / Assert
    assertNotNull(statsDTO);
    assertNotNull(statsDTO.getOriginalUrl());
    assertNotNull(statsDTO.getShortenedUrl());
    assertNotNull(statsDTO.getAccessLogs());

    assertEquals(1, statsDTO.getAccessLogs().size());
  }

  @DisplayName(
      "Integration test given pagination parameters when list all then should"
          + " return url list paginated")
  @Test
  @Order(5)
  void
      integrationTestGivenPaginationParameters_WhenListAll_ThenShouldReturnUrlListPaginated()
          throws IOException {

    // Given / Arrange
    ResponseOptions<?> response =
        given()
            .spec(requestSpecification)
            .param("size", "10")
            .param("sort", "createdAt,asc")
            .when()
            .get()
            .andReturn();

    // When / Act
    PaginatedResponseDTO<UrlResponseDTO> paginatedResponseDTO =
        mapper.readValue(
            response.body().asString(),
            new TypeReference<PaginatedResponseDTO<UrlResponseDTO>>() {});

    // Then / Assert
    assertNotNull(paginatedResponseDTO);

    assertTrue(paginatedResponseDTO.getPage() >= 0);
    assertTrue(paginatedResponseDTO.getSize() >= 0);
    assertTrue(paginatedResponseDTO.getTotalElements() >= 0);
    assertTrue(paginatedResponseDTO.getTotalPages() >= 0);

    assertNotNull(paginatedResponseDTO.getContent());

    assertEquals(1, paginatedResponseDTO.getContent().size());
  }

  @DisplayName(
      "Integration test given urlId when delete by id then should return no"
          + " content")
  @Test
  @Order(6)
  void integrationTestGivenUrlId_WhenDeleteById_ThenShouldReturnNoContent() {

    // Given / Arrange

    Response response =
        given()
            .spec(requestSpecification)
            .delete("/{urlId}", urlResponseDTO.getId());

    // When / Act / Then / Assert
    assertEquals(response.statusCode(), HttpStatus.NO_CONTENT.value());
  }
}
