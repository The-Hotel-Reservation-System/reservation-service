package com.example.reservationservice.controller;

import com.example.reservationservice.model.dto.ReservationDto;
import com.example.reservationservice.model.request.CreateReservationRequest;
import com.example.reservationservice.service.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class ReservationControllerTest {
  private ReservationController reservationController;
  @Mock
  private ReservationService reservationService;

  @BeforeEach
  void init() {
    reservationController = new ReservationControllerImpl(reservationService);
    reservationService = Mockito.mock(ReservationService.class);
  }

  @Test
  public void getReservationById_shouldWork() {
    Mockito.when(reservationService.getReservation(Mockito.any(String.class)))
        .thenReturn(ReservationDto.builder().build());
    ResponseEntity<ReservationDto> responses = reservationController.getReservation(Mockito.any(String.class));
    Assertions.assertEquals(HttpStatus.OK, responses.getStatusCode());
  }

  @Test
  public void deleteReservationById_shouldWork() {
    Mockito.when(reservationService.getReservation(Mockito.any(String.class)))
        .thenReturn(ReservationDto.builder().build());
    ResponseEntity<Boolean> responses = reservationController.deleteReservation(Mockito.any(String.class));
    Assertions.assertEquals(HttpStatus.OK, responses.getStatusCode());
  }

  @Test
  public void createReservationById_shouldWork() {
    Mockito.when(reservationService.getReservation(Mockito.any(String.class)))
        .thenReturn(ReservationDto.builder().build());
    ResponseEntity<ReservationDto> responses = reservationController.createReservation(Mockito.any(CreateReservationRequest.class));
    Assertions.assertEquals(HttpStatus.OK, responses.getStatusCode());
  }

  @Test
  public void getReservationByGuestId_shouldWork() {
    List<ReservationDto> list = new ArrayList<>();
    list.add(ReservationDto.builder().build());
    Mockito.when(reservationService.getReservationByGuestId(Mockito.any(String.class)))
        .thenReturn(list);
    ResponseEntity<List<ReservationDto>> responses = reservationController.getReservationByGuestId(Mockito.any(String.class));
    Assertions.assertEquals(HttpStatus.OK, responses.getStatusCode());
  }
}
