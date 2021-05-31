package com.example.reservationservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation", schema = "public")
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger id;
  private BigInteger hotelId;
  private BigInteger guestId;
  private String roomType;
  private Instant createdDate;
  private Instant updatedDate;
  private Instant startDate;
  private Instant endDate;
  private String total;
  private String status;
  private BigInteger roomId;
}
