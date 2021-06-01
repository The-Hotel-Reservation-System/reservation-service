package com.example.reservationservice.controller;

import com.example.reservationservice.model.dto.ReservationDto;
import com.example.reservationservice.model.request.CreateReservationRequest;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import javax.validation.Valid;


@CircuitBreaker(name = "reservation")
@Retry(name = "reservation")
@RateLimiter(name = "reservation")
@TimeLimiter(name = "reservation")
@Bulkhead(name = "reservation")
@RequestMapping("/api/reservation")
public interface ReservationController {
  /**
   * Get the reservation with specific id
   * @param reservationId the id of reservation
   * @return ReservationDto the hotel response model {@link ReservationDto}
   */
  @GetMapping("/{reservationId}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found reservation.",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = ReservationDto.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied.",
          content = @Content),
      @ApiResponse(responseCode = "404", description = "reservation not found.",
          content = @Content)
  })
  ResponseEntity<ReservationDto> getReservation(@PathVariable("reservationId") String reservationId);

  /**
   *
   * @param request request create reservation
   * @return ReservationDto reservation model response {@link ReservationDto}
   */
  @PostMapping
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "created reservation.",
          content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = ReservationDto.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied.",
          content = @Content)
  })
  ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody
                                                       CreateReservationRequest request);

  /**
   *
   * @param reservationId the id of hotel
   * @return Boolean
   */
  @DeleteMapping("/{reservationId}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reservation is deleted."),
      @ApiResponse(responseCode = "400", description = "Failed to delete reservation.")
  })
  ResponseEntity<Boolean> deleteReservation(@PathVariable("reservationId") String reservationId);

  /**
   * Get the reservation with specific id of guest
   * @param guestId the id of guest
   * @return ReservationDto the hotel response model {@link ReservationDto}
   */
  @GetMapping("/guest/{guestId}")
  ResponseEntity<List<ReservationDto>> getReservationByGuestId(String guestId);
}
