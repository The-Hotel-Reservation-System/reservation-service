package com.example.reservationservice.model;

public enum ReservationStatus {
  COMPLETE("Complete"),
  BOOKED("Booked"),
  CANCELED("Canceled");

  ReservationStatus(String status) {
    this.status = status;
  }

  private String status;

  public String getStatus() {
    return status;
  }
}
