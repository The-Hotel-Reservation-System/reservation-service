package com.example.reservationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestDto {
  private BigInteger id;
  private String name;
  private String email;
  private String phone;
}
