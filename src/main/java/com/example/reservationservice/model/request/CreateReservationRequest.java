package com.example.reservationservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;
import java.time.Instant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class CreateReservationRequest {
  @NotNull(message = "hotelId should not be null")
  private BigInteger hotelId;
  @NotNull(message = "guestId should not be null")
  private BigInteger guestId;
  @NotBlank(message = "roomType should not be blank")
  private String roomType;
  @NotNull(message = "startDate should not be null")
  private Instant startDate;
  @NotNull(message = "endDate should not be null")
  private Instant endDate;
}
