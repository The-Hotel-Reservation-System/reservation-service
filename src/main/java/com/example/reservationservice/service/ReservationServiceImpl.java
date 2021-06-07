package com.example.reservationservice.service;

import com.example.reservationservice.connector.GuestService;
import com.example.reservationservice.connector.HotelService;
import com.example.reservationservice.exception.ReservationExceptionResponse;
import com.example.reservationservice.exception.ReservationServiceException;
import com.example.reservationservice.model.ReservationStatus;
import com.example.reservationservice.model.dto.ReservationDto;
import com.example.reservationservice.model.dto.RoomDto;
import com.example.reservationservice.model.entity.Reservation;
import com.example.reservationservice.model.request.CreateReservationRequest;
import com.example.reservationservice.repository.ReservationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

  private ReservationRepository reservationRepository;
  private HotelService hotelService;
  private GuestService guestService;

  public ReservationServiceImpl(ReservationRepository reservationRepository,
                                HotelService hotelService,
                                GuestService guestService) {
    this.reservationRepository = reservationRepository;
    this.hotelService = hotelService;
    this.guestService = guestService;
  }

  @Override
  public ReservationDto getReservation(String reservationId) {
    log.info("Get reservation {}", reservationId);
    return reservationRepository.findById(new BigInteger(reservationId))
        .map(this::convertReservationToReservationDto)
        .orElseThrow(() -> {
          log.error("Cannot find reservation {}", reservationId);
          return new ReservationServiceException(ReservationExceptionResponse.RESERVATION_NOT_FOUND);
        });
  }

  @Override
  public Boolean deleteReservation(String reservationId) {
    log.info("Delete reservation {}", reservationId);
    return reservationRepository.findById(new BigInteger(reservationId))
        .map(reservation -> {
          reservationRepository.delete(reservation);
          return Boolean.TRUE;
        })
        .orElseThrow(() -> {
          log.error("Cannot find reservation {}", reservationId);
          return new ReservationServiceException(ReservationExceptionResponse.RESERVATION_NOT_FOUND);
        });
  }

  @CircuitBreaker(name = "room", fallbackMethod = "roomFallback")
  @Override
  public ReservationDto createReservation(CreateReservationRequest request) {
    log.info("Create reservation with request {}", request);
    var guest = guestService.getGuestById(request.getGuestId().toString());
    if (guest == null) {
      throw new ReservationServiceException(ReservationExceptionResponse.GUEST_NOT_FOUND);
    }
    var room = Optional.ofNullable(getAvailableRoom(request))
        .orElseThrow(() -> {
          log.error("No room is available with request {}", request);
          return new ReservationServiceException(ReservationExceptionResponse.NO_ROOM_AVAILABLE);
        });
    var reservation = buildReservation(request, room);
    return convertReservationToReservationDto(reservationRepository.save(reservation));
  }

  public ReservationDto roomFallback(CreateReservationRequest request, Throwable t) {
    log.info("Room fallback: {}", request);
    return null;
  }

  @Override
  public List<ReservationDto> getReservationByGuestId(String guestId) {
    log.info("Get reservation by guest {}", guestId);
    return reservationRepository.findByGuestId(guestId)
        .stream()
        .map(this::convertReservationToReservationDto)
        .collect(Collectors.toList());
  }

  private RoomDto getAvailableRoom(CreateReservationRequest request) {
    var listRoom = hotelService
        .getRoomsByHotelId(request.getHotelId().toString())
        .stream()
        .filter(roomDto -> roomDto.getType().equals(request.getRoomType()))
        .collect(Collectors.toList());

    var busyRoom = reservationRepository.getRoomBookedFromStartDateToEndDate(request.getStartDate(),
                                                                             request.getEndDate(),
                                                                             request.getRoomType(),
                                                                             request.getHotelId());
    List<RoomDto> availableRoom = new ArrayList<>();
    availableRoom.addAll(listRoom
                             .stream()
                             .filter(roomDto -> !busyRoom.contains(roomDto.getId()))
                             .collect(Collectors.toList()));

    if (availableRoom.isEmpty()) {
      return null;
    }

    return availableRoom.get(0);
  }

  private BigInteger calculateStayDuration(Instant startDate, Instant endDate) {
    return BigInteger.valueOf(ChronoUnit.DAYS.between(startDate, endDate) + 1);
  }

  private BigInteger calculatePrice(String price, BigInteger duration) {
    return new BigInteger(price).multiply(duration);
  }

  private Reservation buildReservation(CreateReservationRequest request, RoomDto room) {

    BigInteger total = calculateMoney(request, room);

    return Reservation.builder()
        .total(total.toString())
        .startDate(request.getStartDate())
        .roomType(request.getRoomType())
        .hotelId(request.getHotelId())
        .guestId(request.getGuestId())
        .endDate(request.getEndDate())
        .createdDate(Instant.now())
        .updatedDate(Instant.now())
        .status(ReservationStatus.BOOKED.getStatus())
        .roomId(new BigInteger(room.getId()))
        .build();
  }

  private BigInteger calculateMoney(CreateReservationRequest request, RoomDto room) {
    var duration = calculateStayDuration(request.getStartDate(), request.getEndDate());
    return calculatePrice(room.getPrice(), duration);
  }

  private ReservationDto convertReservationToReservationDto(Reservation reservation) {
    return ReservationDto.builder()
        .createdDate(reservation.getCreatedDate())
        .endDate(reservation.getEndDate())
        .guestId(reservation.getGuestId().toString())
        .hotelId(reservation.getHotelId().toString())
        .id(reservation.getId())
        .roomType(reservation.getRoomType())
        .startDate(reservation.getStartDate())
        .total(reservation.getTotal())
        .updatedDate(reservation.getUpdatedDate())
        .roomId(reservation.getRoomId().toString())
        .status(reservation.getStatus())
        .build();
  }
}
