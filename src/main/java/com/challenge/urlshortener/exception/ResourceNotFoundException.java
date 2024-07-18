package com.challenge.urlshortener.exception;

public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(ExceptionDefinition exceptionDefinition) {
    super(exceptionDefinition);
  }
}
