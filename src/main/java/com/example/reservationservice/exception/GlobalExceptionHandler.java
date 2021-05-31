package com.example.reservationservice.exception;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @Autowired
  ReservationServiceConverter converter;

  @Autowired
  private Tracer tracer;

  @ExceptionHandler(ReservationServiceException.class)
  public ResponseEntity<Object> handleHotelException(ReservationServiceException exception) {
    log.error("HotelException", exception);
    return new ResponseEntity<>(
        converter.toJsonNode(exception.getReservationExceptionResponse(), StringUtils.EMPTY, tracer),
        new HttpHeaders(),
        exception.getReservationExceptionResponse().getHttpStatus()
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleInvalidArgumentException(
      MethodArgumentNotValidException exception)
      throws JsonProcessingException {
    log.error("MethodArgumentNotValidException", exception);
    return new ResponseEntity<>(
        converter.toJsonNode(ReservationExceptionResponse.INVALID_ARGUMENT,
                             exception
                                 .getBindingResult()
                                 .getAllErrors()
                                 .get(NumberUtils.INTEGER_ZERO)
                                 .getDefaultMessage(),
                             tracer
        ),
        new HttpHeaders(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<Object> feignException(FeignException e) throws JsonProcessingException {
    log.error("FeignException {}", e);
    var errorMessageJson = converter.toJsonNode(e.getMessage(), tracer);
    return new ResponseEntity<>(
        errorMessageJson, new HttpHeaders(), HttpStatus.resolve(e.status()));
  }
}
