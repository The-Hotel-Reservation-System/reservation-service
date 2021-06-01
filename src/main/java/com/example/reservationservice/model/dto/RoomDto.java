package com.example.reservationservice.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoomDto {
  private String id;
  private String type;
  private String price;
  private String status;
  private String hotel;
}
