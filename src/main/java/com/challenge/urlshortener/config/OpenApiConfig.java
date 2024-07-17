package com.challenge.urlshortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("URL Shortener")
                .version("v1")
                .contact(
                    new Contact()
                        .url("https://www.linkedin.com/in/joaopaulo-chaves/")
                        .email("jpcchaves@outlook.com"))
                .description(
                    "REST API built to create shortened URLS")
                .termsOfService("https://jpcchaves-dev.netlify.app")
                .license(
                    new License()
                        .name("MIT")
                        .url("https://jpcchaves-dev.netlify.app")));
  }

}
