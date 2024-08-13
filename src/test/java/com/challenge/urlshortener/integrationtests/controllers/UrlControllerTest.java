package com.challenge.urlshortener.integrationtests.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.challenge.urlshortener.config.CustomLocalDateTimeSerializable;
import com.challenge.urlshortener.config.TestConfigs;
import com.challenge.urlshortener.domain.dto.UrlRequestDTO;
import com.challenge.urlshortener.domain.dto.UrlResponseDTO;
import com.challenge.urlshortener.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.time.LocalDateTime;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.module.SimpleModule;

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

    SimpleModule module = new SimpleModule();

    module.addDeserializer(
        LocalDateTime.class,
        CustomLocalDateTimeSerializable.CUSTOM_LOCAL_DATE_TIME_DESERIALIZER);

    module.addSerializer(
        LocalDateTime.class,
        CustomLocalDateTimeSerializable.CUSTOM_LOCAL_DATE_TIME_SERIALIZER);

    faker = new Faker();

    mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.registerModule(module);

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
    System.out.println(urlResponseDTO);
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
}
