package com.example.reservationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigInteger;

@Builder
@AllArgsConstructor
@Getter
public class GuestDto {
  private BigInteger id;
  private String name;
  private String email;
  private String phone;
}
