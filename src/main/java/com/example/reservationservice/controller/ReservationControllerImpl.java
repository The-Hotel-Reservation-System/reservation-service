package com.example.reservationservice.controller;

import com.example.reservationservice.model.dto.ReservationDto;
import com.example.reservationservice.model.request.CreateReservationRequest;
import com.example.reservationservice.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationControllerImpl implements ReservationController {
  private ReservationService reservationService;

  public ReservationControllerImpl(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @Override
  public ResponseEntity<ReservationDto> getReservation(String reservationId) {
    return ResponseEntity.ok(reservationService.getReservation(reservationId));
  }

  @Override
  public ResponseEntity<ReservationDto> createReservation(CreateReservationRequest request) {
    return ResponseEntity.ok(reservationService.createReservation(request));
  }

  @Override
  public ResponseEntity<Boolean> deleteReservation(String reservationId) {
    return ResponseEntity.ok(reservationService.deleteReservation(reservationId));
  }

  @Override
  public ResponseEntity<List<ReservationDto>> getReservationByGuestId(String guestId) {
    return ResponseEntity.ok(reservationService.getReservationByGuestId(guestId));
  }

}
