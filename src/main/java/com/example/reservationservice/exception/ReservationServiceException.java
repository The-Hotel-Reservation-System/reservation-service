package com.example.reservationservice.exception;

public class ReservationServiceException extends RuntimeException {
  final ReservationExceptionResponse reservationExceptionResponse;

  public ReservationServiceException(ReservationExceptionResponse reservationExceptionResponse) {
    this.reservationExceptionResponse = reservationExceptionResponse;
  }

  public ReservationExceptionResponse getReservationExceptionResponse() {
    return reservationExceptionResponse;
  }
}
