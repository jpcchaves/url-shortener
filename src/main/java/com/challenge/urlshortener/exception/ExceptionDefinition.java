package com.challenge.urlshortener.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionDefinition {

  URL0001("URL not found with the given shortened url!", "URL-0001", HttpStatus.NOT_FOUND.value());

  private String message;
  private String code;
  private int httpStatus;

  ExceptionDefinition(
      String message,
      String code,
      int httpStatus
  ) {
    this.message = message;
    this.code = code;
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(int httpStatus) {
    this.httpStatus = httpStatus;
  }
}
