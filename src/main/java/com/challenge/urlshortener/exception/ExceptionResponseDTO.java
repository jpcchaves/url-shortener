package com.challenge.urlshortener.exception;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class ExceptionResponseDTO implements Serializable {
  @Serial private static final long serialVersionUID = -404456896252955618L;

  private final Date timestamp = new Date();
  private String message;
  private String details;

  public ExceptionResponseDTO() {}

  public ExceptionResponseDTO(String message, String details) {
    this.message = message;
    this.details = details;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public Date getTimestamp() {
    return timestamp;
  }
}
