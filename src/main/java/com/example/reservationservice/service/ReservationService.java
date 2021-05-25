package com.example.reservationservice.service;

import com.example.reservationservice.model.dto.ReservationDto;
import com.example.reservationservice.model.request.CreateReservationRequest;

import java.util.List;

public interface ReservationService {

  ReservationDto getReservation(String reservationId);

  Boolean deleteReservation(String reservationId);

  ReservationDto createReservation(CreateReservationRequest request);

  List<ReservationDto> getReservationByGuestId(String guestId);
}
