package com.example.reservationservice.service;

import com.example.reservationservice.connector.GuestService;
import com.example.reservationservice.connector.HotelService;
import com.example.reservationservice.exception.ReservationServiceException;
import com.example.reservationservice.model.ReservationStatus;
import com.example.reservationservice.model.dto.ReservationDto;
import com.example.reservationservice.model.dto.RoomDto;
import com.example.reservationservice.model.entity.Reservation;
import com.example.reservationservice.model.request.CreateReservationRequest;
import com.example.reservationservice.repository.ReservationRepository;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ReservationServiceTest {

  private String STATUS;
  private ReservationService reservationService;
  private ReservationDto reservationDtoModel;
  private Reservation reservation;
  private CreateReservationRequest createReservationRequest;
  List<RoomDto> roomsDto = new ArrayList<>();
  List<ReservationDto> reservationsModel = new ArrayList<>();
  private Instant DATE = Instant.parse("1995-10-23T10:12:35Z");
  private BigInteger GUEST_ID = BigInteger.ONE;
  private BigInteger HOTEL_ID = BigInteger.ONE;
  private BigInteger ID = BigInteger.ONE;
  private final String PRICE = "12345";
  private String ROOM_TYPE = "ROOM_TYPE";
  private String TOTAL = "TOTAL";
  private String HOTEL_NAME = "Hotel name";

  @Mock
  ReservationRepository reservationRepository;
  @Mock
  HotelService hotelService;
  @Mock
  GuestService guestService;

  @BeforeEach
  void init() {
    reservation = buildReservation();
    reservationService = new ReservationServiceImpl(reservationRepository, hotelService, guestService);
    reservationDtoModel = buildReservationDto();
    createReservationRequest = buildReservationRequest();
    roomsDto.add(buildRoomDtoModel());
    reservationsModel.add(buildReservationDto());
  }

  @Test
  void getReservation_shouldWork() {
    Mockito.when(reservationRepository.findById(Mockito.any(BigInteger.class))).thenReturn(Optional.of(reservation));
    ReservationDto reservationDto = reservationService.getReservation("123");
    Assertions.assertEquals(reservationDtoModel, reservationDto);
  }

  @Test
  void getReservation_NotFound_shouldThrowException() {
    Mockito.when(reservationRepository.findById(Mockito.any(BigInteger.class))).thenReturn(Optional.empty());
    Assertions.assertThrows(ReservationServiceException.class, () -> reservationService.getReservation("123"));
  }

  @Test
  void deleteReservation_shouldWork() {
    Mockito.when(reservationRepository.findById(Mockito.any(BigInteger.class))).thenReturn(Optional.of(reservation));
    Mockito.doNothing().when(reservationRepository).delete(Mockito.any(Reservation.class));
    Boolean result = reservationService.deleteReservation("123");
    Assertions.assertEquals(Boolean.TRUE, result);
  }

  @Test
  void deleteReservation_NotFound_shouldThrowException() {
    Mockito.when(reservationRepository.findById(Mockito.any(BigInteger.class))).thenReturn(Optional.empty());
    Assertions.assertThrows(ReservationServiceException.class, () -> reservationService.deleteReservation("123"));
  }

  @Test
  void createReservation_shouldWork() {
    List<String> ids = new ArrayList<>();
    ids.add(ID.toString());
    Mockito.when(hotelService.getRoomsByHotelId(Mockito.any(String.class))).thenReturn(roomsDto);
    Mockito.when(reservationRepository.getRoomBookedFromStartDateToEndDate(Mockito.any(Instant.class),
                                                                           Mockito.any(Instant.class),
                                                                           Mockito.any(String.class),
                                                                           Mockito.any(BigInteger.class)))
        .thenReturn(List.of());
    Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation);
    ReservationDto reservationDto = reservationService.createReservation(createReservationRequest);
    Assertions.assertEquals(reservationDtoModel, reservationDto);
  }

  @Test
  void createReservation_NoRoomAvailable_shouldWork() {
    List<String> ids = new ArrayList<>();
    ids.add(ID.toString());
    var temp = reservation;
    Mockito.when(hotelService.getRoomsByHotelId(Mockito.any(String.class))).thenReturn(roomsDto);
    Mockito.when(reservationRepository.getRoomBookedFromStartDateToEndDate(Mockito.any(Instant.class),
                                                                           Mockito.any(Instant.class),
                                                                           Mockito.any(String.class),
                                                                           Mockito.any(BigInteger.class)))
        .thenReturn(ids);
    Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(reservation);
    Assertions.assertThrows(ReservationServiceException.class, () -> reservationService.createReservation(createReservationRequest));
  }

  @Test
  void createReservation_HotelNotFoundAvailable_shouldWork() {
    Mockito.when(hotelService.getRoomsByHotelId(Mockito.any(String.class))).thenThrow(FeignException.class);
    Assertions.assertThrows(FeignException.class, () -> reservationService.createReservation(createReservationRequest));
  }

  @Test
  void getReservationByGuestId_shouldWork() {
    List<Reservation> list = new ArrayList<>();
    list.add(reservation);
    Mockito.when(reservationRepository.findByGuestId(Mockito.anyString())).thenReturn(list);
    var reservations = reservationService.getReservationByGuestId("123456789");
    Assertions.assertEquals(reservationsModel, reservations);
  }

  private RoomDto buildRoomDtoModel() {
    return RoomDto.builder()
        .id(ID.toString())
        .type(ROOM_TYPE)
        .price(PRICE)
        .build();
  }

  private CreateReservationRequest buildReservationRequest() {
    return CreateReservationRequest.builder()
        .endDate(DATE)
        .hotelId(HOTEL_ID)
        .guestId(GUEST_ID)
        .roomType(ROOM_TYPE)
        .startDate(DATE)
        .build();
  }

  private Reservation buildReservation() {
    return Reservation.builder()
        .createdDate(DATE)
        .endDate(DATE)
        .guestId(GUEST_ID)
        .hotelId(HOTEL_ID)
        .id(ID)
        .roomId(ID)
        .roomType(ROOM_TYPE)
        .startDate(DATE)
        .total(TOTAL)
        .updatedDate(DATE)
        .status(ReservationStatus.BOOKED.getStatus())
        .roomId(ID)
        .build();
  }

  private ReservationDto buildReservationDto() {
    return ReservationDto.builder()
        .createdDate(DATE)
        .endDate(DATE)
        .guestId(GUEST_ID.toString())
        .hotelId(HOTEL_ID.toString())
        .id(ID)
        .roomType(ROOM_TYPE)
        .startDate(DATE)
        .total(TOTAL)
        .updatedDate(DATE)
        .roomId(ID.toString())
        .status(ReservationStatus.BOOKED.getStatus())
        .build();
  }


}
