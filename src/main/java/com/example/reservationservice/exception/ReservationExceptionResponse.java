package com.example.reservationservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationExceptionResponse {
  RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "3000", "Reservation is not found."),
  UNHANDLED_EXCEPTION(HttpStatus.BAD_REQUEST, "3001", "Unhandled exception."),
  NO_ROOM_AVAILABLE(HttpStatus.NOT_FOUND, "3002", "No room is available."),
  INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "3003", "");

  ReservationExceptionResponse(HttpStatus httpStatus, String errorCode, String errorMessage) {
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  private HttpStatus httpStatus;
  private String errorCode;
  private String errorMessage;

}
