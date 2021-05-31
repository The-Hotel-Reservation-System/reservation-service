package com.example.reservationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ReservationDto {
  private BigInteger id;
  private String hotelId;
  private String guestId;
  private String roomId;
  private String roomType;
  private Instant createdDate;
  private Instant updatedDate;
  private Instant startDate;
  private Instant endDate;
  private String total;
  private String status;
}
