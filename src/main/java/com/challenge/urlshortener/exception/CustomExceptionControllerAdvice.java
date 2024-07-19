package com.challenge.urlshortener.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice
public class CustomExceptionControllerAdvice extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(
      CustomExceptionControllerAdvice.class);

  @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
  @Override
  public ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request
  ) {

    logger.error(ex.getMessage());
    logger.error(Arrays.toString(ex.getStackTrace()));

    ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

    exceptionResponseDTO.setMessage(ex.getMessage());
    exceptionResponseDTO.setDetails(request.getDescription(false));

    return new ResponseEntity<>(exceptionResponseDTO,
                                HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler
  public ResponseEntity<ExceptionResponseDTO> handleResourceNotFoundException(
      ResourceNotFoundException ex,
      WebRequest request
  ) {

    logger.error(ex.getMessage());
    logger.error(Arrays.toString(ex.getStackTrace()));

    ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

    exceptionResponseDTO.setMessage(ex.getMessage());
    exceptionResponseDTO.setDetails(request.getDescription(false));

    return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.NOT_FOUND);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {
    ExceptionResponseDTO exceptionResponse =
        new ExceptionResponseDTO(
            Objects.requireNonNull(ex.getFieldError())
                   .getDefaultMessage(),
            request.getDescription(false));

    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }
}
