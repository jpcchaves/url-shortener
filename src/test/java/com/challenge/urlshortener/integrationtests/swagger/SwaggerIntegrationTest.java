package com.challenge.urlshortener.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.challenge.urlshortener.config.TestConfigs;
import com.challenge.urlshortener.integrationtests.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

  @DisplayName("Junit test for should display Swagger UI Page")
  @Test
  void testShouldDisplaySwaggerUiPage() {

    String content =
        given()
            .basePath("/swagger-ui/index.html")
            .port(TestConfigs.SERVER_PORT)
            .when()
            .get()
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .asString();

    assertTrue(content.contains("Swagger UI"));
  }
}
